# Быстрый запуск UniHub Backend

Эта инструкция поможет быстро запустить проект локально для демонстрации работы на платформе MAX.

## Что делает приложение

UniHub - это единая цифровая платформа для студентов, которая объединяет:

- Новости университета
- Расписание занятий с типами занятий (лекции, практики, семинары)
- Студенческие клубы с категориями (спорт, технологии, наука и др.)
- Проекты и стажировки
- Электронные справки

Система автоматически показывает студенту персонализированную информацию его университета.

## Вариант 1: Полный запуск в Docker (рекомендуется)

Этот способ запускает весь стек приложения в контейнерах.

### Шаг 1: Подготовка

Убедитесь что у вас установлен Docker и Docker Compose:

```bash
docker --version
docker-compose --version
```

### Шаг 2: Клонирование

```bash
git clone <repository-url>
cd backend
```

### Шаг 3: Настройка окружения

Создайте файл `.env`:

```bash
cat > .env << 'EOF'
POSTGRES_DB=unihub_db
POSTGRES_USER=unihub_user
POSTGRES_PASSWORD=unihub_pass
MINIO_ACCESS_KEY=minioadmin
MINIO_SECRET_KEY=minioadmin123
EOF
```

### Шаг 4: Запуск

```bash
# Сборка и запуск всех сервисов
docker-compose -f prod.compose.yaml up -d --build

# Ожидание готовности (примерно 2-3 минуты)
echo "Ожидание запуска сервисов..."
sleep 120

# Проверка статуса
docker-compose -f prod.compose.yaml ps
```

### Шаг 5: Инициализация MinIO

```bash
# Создание bucket для файлов
docker exec -it unihub-minio mc alias set local http://localhost:9000 minioadmin minioadmin123
docker exec -it unihub-minio mc mb local/unihub
docker exec -it unihub-minio mc anonymous set public local/unihub
```

### Шаг 6: Проверка работы

```bash
# Проверка здоровья приложения
curl http://localhost:8080/actuator/health

# Ожидаемый ответ: {"status":"UP"}
```

Откройте в браузере:

- API документация: http://localhost:8080/swagger-ui.html
- MinIO Console: http://localhost:9001 (minioadmin / minioadmin123)

## Вариант 2: Локальная разработка

Этот способ запускает только инфраструктуру в Docker, а приложение локально.

### Шаг 1: Проверка Java

```bash
java -version
# Должна быть версия 21 или выше
```

Если Java не установлена:

- macOS: `brew install openjdk@21`
- Ubuntu: `sudo apt install openjdk-21-jdk`
- Windows: скачайте с https://adoptium.net/

### Шаг 2: Запуск инфраструктуры

```bash
# Запуск PostgreSQL и MinIO
docker-compose up -d

# Ожидание готовности
sleep 30

# Проверка
docker-compose ps
```

### Шаг 3: Инициализация MinIO

```bash
# Через Docker
docker run --rm --network=host minio/mc alias set local http://localhost:9000 minioadmin minioadmin
docker run --rm --network=host minio/mc mb local/unihub
docker run --rm --network=host minio/mc anonymous set public local/unihub
```

### Шаг 4: Запуск приложения

```bash
# Сборка и запуск
./gradlew bootRun
```

Приложение запустится на http://localhost:8080

## Демонстрация работы

### 1. Проверка API через Swagger

Откройте http://localhost:8080/swagger-ui.html

Вы увидите все доступные эндпоинты:

- Управление новостями
- Работа с клубами
- Проекты и стажировки
- Расписание занятий
- Справки студентов

### 2. Проверка базы данных

```bash
# Подключение к PostgreSQL
docker exec -it unihub-postgres psql -U unihub_user -d unihub_db

# Просмотр таблиц
\dt

# Примеры запросов
SELECT * FROM users LIMIT 5;
SELECT * FROM clubs;
SELECT * FROM projects;

# Выход
\q
```

### 3. Проверка файлового хранилища

Откройте MinIO Console: http://localhost:9001

- Логин: minioadmin
- Пароль: minioadmin123 (или minioadmin для локальной разработки)

Вы увидите bucket `unihub` для хранения изображений и документов.

### 4. Тестовые запросы API

Примеры работы с API (требуется JWT токен для авторизации):

```bash
# Получить список клубов (демо без авторизации)
curl http://localhost:8080/api/student/clubs

# Получить расписание
curl http://localhost:8080/api/student/schedule

# Получить список проектов
curl http://localhost:8080/api/student/projects
```

Для полноценной работы требуется авторизация через платформу MAX.

## Демонстрируемая функциональность

### Основные возможности

1. **Управление пользователями**
    - Студенты, администраторы университетов
    - Ролевая модель доступа
    - Интеграция с платформой MAX

2. **Работа с контентом**
    - Новости с изображениями (хранятся в MinIO)
    - Клубы с категориями (Спорт, Технологии, Наука и др.)
    - Проекты со статусами (Активен, Набор, Завершен)

3. **Расписание**
    - Занятия с типами (Лекция, Практика, Семинар)
    - Автоматическое определение ближайшего занятия
    - Группировка по расписаниям

4. **Стажировки**
    - Информация о компаниях
    - Условия и сроки
    - Фильтрация по университету

5. **Электронные справки**
    - Запрос справок онлайн
    - Отслеживание статуса
    - Скачивание готовых документов

### Технологические особенности

- Оптимизированные запросы к БД через EntityGraph (нет проблемы N+1)
- Enum типы для типобезопасности
- MinIO для хранения файлов с presigned URLs
- Автоматическая документация API через Swagger
- Healthcheck эндпоинты для мониторинга
- Liquibase для версионирования БД

## Остановка

```bash
# Остановка всех сервисов
docker-compose -f prod.compose.yaml down

# Или для локальной разработки
docker-compose down

# Удаление данных (если нужно начать с чис��ого листа)
docker-compose -f prod.compose.yaml down -v
```

## Логи и отладка

### Просмотр логов

```bash
# Все сервисы
docker-compose -f prod.compose.yaml logs -f

# Только backend
docker-compose -f prod.compose.yaml logs -f backend

# Последние 100 строк
docker-compose -f prod.compose.yaml logs --tail=100 backend
```

### Распространенные проблемы

**Про��лема: Backend не запускается**

```bash
# Проверьте логи
docker logs unihub-backend

# Проверьте что PostgreSQL готова
docker exec unihub-postgres pg_isready
```

**Проблема: Порты заняты**

```bash
# Найдите процесс на порту
lsof -i :8080
lsof -i :5432

# Остановите конфликтующий процесс или измените порты в compose.yaml
```

**Проблема: MinIO bucket не создан**

```bash
# Создайте вручную через UI или CLI
docker exec -it unihub-minio mc mb local/unihub
```

## Интеграция с чат-ботом MAX

Приложение предоставляет REST API для интеграции с чат-ботом платформы MAX:

1. Все эндпоинты требуют JWT токен от MAX
2. API возвращает данные в формате JSON
3. Поддерживается фильтрация по университету пользователя
4. Автоматическая валидация и обработка ошибок

Примеры интеграции и TypeScript клиенты находятся в папке `frontend/`.

## Веб-интерфейс

Веб-интерфейс (фронтенд) разрабатывается отдельно и подключается через:

- Готовые TypeScript интерфейсы из `frontend/types-all.ts`
- HTTP клиенты из `frontend/api-services-ready.ts`
- Документация в `frontend/API_DOCUMENTATION.md`

API бэкенда полностью готов для подключения фронтенда.

## Дополнительная информация

- Полная документация: см. `README.md`
- Системные требования: см. `REQUIREMENTS.md`
- Итоги работы: см. `FINAL_REPORT.md`
- API документация для фронтенда: см. `frontend/API_DOCUMENTATION.md`

## Готовый Docker образ

Образ доступен в GitHub Container Registry:

```bash
docker pull ghcr.io/<owner>/backend:latest
```

Автоматическая сборка настроена через GitHub Actions при создании тега или ручном запуске workflow.
