# UniHub Backend - Документация для хакатона MAX

## Описание продукта

UniHub Backend - это серверная часть единой цифровой платформы для студентов университетов, разработанная для интеграции
с платформой MAX. Система решает проблему разрозненности информации в университетской среде, объединяя все необходимые
студенту сервисы в одном месте.

## Решаемая задача

Студенты сталкиваются с множеством проблем:

- Информация разбросана по разным системам и чатам
- Сложно найти расписание, новости, информацию о клубах
- Получение справок требует личного посещения деканата
- Нет единой точки доступа к возможностям университета

UniHub решает эти проблемы, предоставляя единый API для доступа ко всей университетской инфраструктуре через чат-бот на
платформе MAX.

## Реализованная функциональность

### Основные модули

1. **Система новостей**
    - Публикация новостей университета с изображениями
    - Автоматическая фильтрация по университету студента
    - Хранение изображений в MinIO S3

2. **Расписание занятий**
    - Персонализированное расписание для каждого студента
    - Типы занятий: Лекция, Практика, Семинар
    - Автоматическое определение ближайшего занятия
    - Расчет времени до начала занятия

3. **Студенческие клубы**
    - 10 категорий клубов (Спорт, Технологии, Наука и др.)
    - Подсчет участников
    - Загрузка логотипов клубов

4. **Проекты**
    - Статусы: Активен, Набор, Завершен
    - Категории проектов (IT, Наука, Бизнес и др.)
    - Информация об авторах проектов

5. **Стажировки**
    - Список актуальных стажировок
    - Информация о компаниях и условиях
    - Фильтрация по университету

6. **Электронные справки**
    - Онлайн запрос справок
    - Отслеживание статуса (В обработке, Готова, Отклонена)
    - Скачивание готовых документов

### Технические возможности

- REST API со Swagger документацией
- JWT авторизация для интеграции с MAX
- Ролевая модель (Студент, Админ университета, Супер-админ)
- Оптимизированные запросы к БД (EntityGraph, нет N+1)
- Типобезопасность через Enum
- Healthcheck эндпоинты для мониторинга
- Версионирование БД через Liquibase

## Запуск продукта локально

### Требования

- Docker 24.0+
- Docker Compose 2.20+
- 4GB RAM минимум
- 5GB свободного места на диске

### Быстрый запуск

```bash
# 1. Клонирование
git clone <repository-url>
cd backend

# 2. Создание .env
cat > .env << 'EOF'
POSTGRES_DB=unihub_db
POSTGRES_USER=unihub_user
POSTGRES_PASSWORD=unihub_pass
MINIO_ACCESS_KEY=minioadmin
MINIO_SECRET_KEY=minioadmin123
EOF

# 3. Запуск всех сервисов
docker-compose -f prod.compose.yaml up -d --build

# 4. Ожидание готовности (2-3 минуты)
sleep 120

# 5. Инициализация MinIO
docker exec -it unihub-minio mc alias set local http://localhost:9000 minioadmin minioadmin123
docker exec -it unihub-minio mc mb local/unihub
docker exec -it unihub-minio mc anonymous set public local/unihub

# 6. Проверка
curl http://localhost:8080/actuator/health
```

### Проверка работы сервиса

1. **API Документация**: http://localhost:8080/swagger-ui.html
2. **MinIO Console**: http://localhost:9001 (minioadmin / minioadin123)
3. **Healthcheck**: `curl http://localhost:8080/actuator/health`

### Демонстрация функционала

```bash
# Просмотр всех таблиц БД
docker exec -it unihub-postgres psql -U unihub_user -d unihub_db -c "\dt"

# Примеры данных
docker exec -it unihub-postgres psql -U unihub_user -d unihub_db -c "SELECT * FROM clubs LIMIT 5;"
docker exec -it unihub-postgres psql -U unihub_user -d unihub_db -c "SELECT * FROM projects LIMIT 5;"

# Просмотр логов
docker logs unihub-backend -f
```

## Docker образ

### Описание Dockerfile

Используется многоступенчатая сборка с оптимизацией:

```dockerfile
# Stage 1: Builder - сборка приложения
FROM bellsoft/liberica-openjdk-debian:21.0.6 AS builder
WORKDIR /application
COPY . .
RUN --mount=type=cache,target=/root/.gradle chmod +x gradlew && ./gradlew clean build -x test

# Stage 2: Layers - извлечение слоев
FROM bellsoft/liberica-openjre-debian:21.0.6 AS layers
WORKDIR /application
COPY --from=builder /application/build/libs/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Stage 3: Runtime - финальный образ
FROM bellsoft/liberica-openjre-debian:21.0.6
VOLUME /tmp
RUN useradd -ms /bin/bash spring-user
USER spring-user
COPY --from=layers /application/dependencies/ ./
COPY --from=layers /application/spring-boot-loader/ ./
COPY --from=layers /application/snapshot-dependencies/ ./
COPY --from=layers /application/application/ ./
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
```

### Преимущества:

- Использование слоев Spring Boot для эффективного кеширования
- Запуск от непривилегированного пользователя
- Встроенный healthcheck
- Оптимизированный размер образа
- Gradle кеш для быстрой пересборки

### Сборка образа

```bash
# Локальная сборка
docker build -t unihub-backend:local .

# Запуск
docker run -p 8080:8080 \
  -e POSTGRES_HOST=postgres \
  -e POSTGRES_DB=unihub_db \
  --network=host \
  unihub-backend:local
```

## CI/CD через GitHub Actions

### Автоматическая сборка и публикация в GHCR

Workflow настроен на:

- Push тегов `v*.*.*` (например, `v1.0.0`)
- Ручной запуск через workflow_dispatch
- Pull requests (без публикации)

### Файл: `.github/workflows/docker-build.yml`

Workflow автоматически:

1. Собирает Docker образ
2. Публикует в GitHub Container Registry
3. Создает мультиплатформенные образы (amd64, arm64)
4. Использует GitHub Actions cache для ускорения
5. Генерирует attestation для безопасности

### Создание релиза

```bash
# Создать тег
git tag -a v1.0.0 -m "Release v1.0.0"

# Отправить в репозиторий
git push origin v1.0.0

# Образ автоматически соберется и опубликуется в:
# ghcr.io/<owner>/backend:v1.0.0
# ghcr.io/<owner>/backend:latest
```

### Ручной запуск сборки

1. Перейти в Actions на GitHub
2. Выбрать "Build and Push Docker Image"
3. Нажать "Run workflow"
4. Указать тег (опционально)

## Интеграция с платформой MAX

### Подключение к чат-боту

1. **Авторизация**: Все эндпоинты требуют JWT токен в заголовке `Authorization: Bearer <token>`
2. **Фильтрация**: Данные автоматически фильтруются по университету пользователя
3. **Форматы**: Все ответы в JSON формате
4. **Документация**: Полная OpenAPI спецификация в Swagger UI

### Основные эндпоинты для чат-бота

```
GET /api/student/news - Получить новости
GET /api/student/schedule - Получить расписание
GET /api/student/clubs - Получить список клубов
GET /api/student/projects - Получить проекты
GET /api/student/internships - Получить стажировки
GET /api/student/certificates - Получить справки
POST /api/student/certificates - Запросить справку
```

### Готовые клиенты для фронтенда

В папке `frontend/` подготовлены:

- `types-all.ts` - TypeScript интерфейсы
- `api-services-ready.ts` - Готовые HTTP клиенты
- `API_DOCUMENTATION.md` - Полная документация
- `README.md` - Руководство по интеграции

## Веб-интерфейс

Backend предоставляет готовое API для подключения веб-интерфейса:

- Swagger UI для тестирования: http://localhost:8080/swagger-ui.html
- REST API эндпоинты
- CORS настроен для локальной разработки
- Готовые TypeScript типы и клиенты

Фронтенд может быть подключен через готовые файлы из папки `frontend/`.

## Сопроводительные материалы

### Документация

- `README.md` - Полная документация по запуску и разработке
- `REQUIREMENTS.md` - Системные требования и зависимости
- `QUICKSTART.md` - Быстрый старт для демонстрации
- `FINAL_REPORT.md` - Итоговый отчет о проделанной работе
- `frontend/API_DOCUMENTATION.md` - Документация API для фронтенда

### Скрипты

- `check-requirements.sh` - Автоматическая проверка системных требований
- `gradlew` - Gradle Wrapper для сборки
- `docker-compose up` - Запуск инфраструктуры
- `docker-compose -f prod.compose.yaml up` - Полный запуск

### Конфигурация

- `Dockerfile` - Оптимизированный Docker образ
- `compose.yaml` - Локальная разработка
- `prod.compose.yaml` - Production конфигурация
- `.github/workflows/docker-build.yml` - CI/CD pipeline
- `.dockerignore` - Оптимизация сборки образа

## Архитектура

### Стек технологий

- **Backend**: Kotlin 2.2.20 + Spring Boot 3.5.7
- **БД**: PostgreSQL 17 Alpine
- **Файлы**: MinIO (S3-compatible)
- **Контейнеризация**: Docker + Docker Compose
- **CI/CD**: GitHub Actions

### Структура сервисов

```
┌─────────────────┐
│   Frontend      │
│  (Web/Bot MAX)  │
└────────┬────────┘
         │ REST API
         ▼
┌─────────────────┐
│   Backend       │
│  Spring Boot    │
│  Port: 8080     │
└────┬──────┬─────┘
     │      │
     ▼      ▼
┌─────────┐ ┌──────────┐
│PostgreSQL│ │  MinIO   │
│Port: 5432│ │Port: 9000│
└─────────┘ └──────────┘
```

### Безопасность

- JWT авторизация
- Ролевая модель доступа
- Непривилегированный пользователь в Docker
- HTTPS в продакшн (через Traefik)
- Healthcheck мониторинг

## Масштабируемость

- Stateless backend (можно запускать несколько инстансов)
- Внешнее хранилище (PostgreSQL, MinIO)
- Docker Compose для оркестрации
- Готов для Kubernetes деплоя
- Мониторинг через actuator endpoints

Продукт полностью готов к запуску и демонстрации на платформе MAX.
Все шаги документированы, скрипты проверены, Docker образ оптимизирован.
