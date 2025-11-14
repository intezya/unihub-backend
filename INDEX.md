# Полная документация UniHub Backend

Это сводный документ со ссылками на всю документацию проекта.

## Основная документация

### 1. README.md

**Назначение**: Главная документация проекта  
**Содержание**: Полное описание, технологический стек, инструкции по запуску, разработке, troubleshooting

### 2. REQUIREMENTS.md

**Назначение**: Системные требования и зависимости  
**Содержание**: Минимальные/рекомендуемые требования, список зависимостей, инструкции по установке

### 3. QUICKSTART.md

**Назначение**: Быстрый старт для демонстрации  
**Содержание**: Краткие инструкции для быстрого запуска, демонстрация функционала, тестовые запросы

### 4. MAX_HACKATHON.md

**Назначение**: Документация для хакатона MAX  
**Содержание**: Описание продукта, решаемая задача, функциональность, интеграция с MAX, Docker образ

### 5. CHECKLIST.md

**Назначение**: Чеклист для проверки готовности  
**Содержание**: Пошаговая проверка перед запуском и демонстрацией

### 6. FINAL_REPORT.md

**Назначение**: Итоговый отчет о работе  
**Содержание**: Что было сделано, результаты, файлы для фронтенда

## Docker и CI/CD

### 7. Dockerfile

**Назначение**: Описание Docker образа  
**Особенности**:

- Многоступенчатая сборка
- Spring Boot layertools
- Непривилегированный пользователь
- Healthcheck
- Оптимизированный размер

### 8. .dockerignore

**Назначение**: Исключение файлов из Docker context  
**Содержание**: Игнорируемые файлы для оптимизации сборки

### 9. compose.yaml

**Назначение**: Docker Compose для локальной разработки  
**Сервисы**: PostgreSQL, MinIO
**Использование**: `docker-compose up -d`

### 10. prod.compose.yaml

**Назначение**: Docker Compose для продакшн  
**Сервисы**: Backend, PostgreSQL, MinIO, Frontend (TODO), Watchtower (TODO)
**Использование**: `docker-compose -f prod.compose.yaml up -d --build`

### 11. .github/workflows/docker-build.yml

**Назначение**: CI/CD pipeline для GitHub Actions  
**Функции**:

- Автоматическая сборка на теги v*.*.*
- Ручной запуск через workflow_dispatch
- Публикация в GHCR
- Мультиплатформенные образы (amd64, arm64)
- GitHub Actions cache

## Скрипты

### 12. check-requirements.sh

**Назначение**: Проверка системных требований  
**Функции**:

- Проверка Java, Docker, Docker Compose
- Проверка свободных портов
- Проверка ресурсов (RAM, CPU, disk)
- Проверка Docker daemon
  **Использование**: `./check-requirements.sh`

### 13. gradlew / gradlew.bat

**Назначение**: Gradle Wrapper  
**Использование**:

- `./gradlew build` - сборка
- `./gradlew bootRun` - запуск
- `./gradlew test` - тесты

## Документация для фронтенда

### 14. frontend/README.md

**Назначение**: Руководство по интеграции для фронтенда  
**Содержание**: Быстрый старт, структура файлов, примеры использования

### 15. frontend/API_DOCUMENTATION.md

**Назначение**: Полная документация API  
**Содержание**: Все эндпоинты, примеры запросов/ответов, типы данных

### 16. frontend/types-all.ts

**Назначение**: TypeScript интерфейсы  
**Содержание**: News, Club, Project, Schedule, Internship, Certificate

### 17. frontend/api-services-ready.ts

**Назначение**: Готовые HTTP клиенты  
**Содержание**: newsService, clubsService, projectsService, scheduleService, internshipsService, certificateService

### 18. frontend/WORK_SUMMARY.md

**Назначение**: Итоги адаптации бэкенда под фронтенд  
**Содержание**: Созданные DTO, обновленные сервисы, контроллеры

### 19. frontend/OPTIMIZATION_SUMMARY.md

**Назначен��е**: Отчет об оптимизации  
**Содержание**: Добавленные Enum, решение LazyInitializationException, EntityGraph

## Конфигурация

### 20. src/main/resources/application.yaml

**Назначение**: Конфигурация Spring Boot  
**Содержание**: База данных, MinIO, Swagger, Server settings

### 21. src/main/resources/db/changelog/db.changelog-master.yaml

**Назначение**: Master файл Liquibase  
**Содержание**: Ссылки на миграции

### 22. src/main/resources/db/changelog/2025/14-01-changelog.sql

**Назначение**: Первоначальные миграции БД

### 23. src/main/resources/db/changelog/2025/14-02-changelog.sql

**Назначение**: Миграции для новых полей  
**Содержание**: status, category, lesson_type, club_members table

### 24. .env (создается вручную)

**Назначение**: Переменные окружения  
**Содержание**: POSTGRES_*, MINIO_*, SPRING_PROFILES_ACTIVE

## Структура проекта

```
backend/
├── src/
│   ├── main/
│   │   ├── kotlin/com/intezya/unihub/
│   │   │   ├── api/
│   │   │   │   ├── controller/     # REST контроллеры
│   │   │   │   └── dto/           # DTO для API
│   │   │   ├── domain/
│   │   │   │   ├── entity/        # JPA Entity
│   │   │   │   └── repository/    # Spring Data JPA
│   │   │   ├── service/           # Бизнес-логика
│   │   │   ├── security/          # JWT, RBAC
│   │   │   ├── config/            # Конфигурация
│   │   │   └── utils/             # Утилиты
│   │   └── resources/
│   │       ├── application.yaml
│   │       └── db/changelog/      # Liquibase миграции
│   └── test/
├── frontend/                       # Готовые материалы для фронтенда
├── .github/workflows/              # CI/CD
├── Dockerfile                      # Docker образ
├── compose.yaml                    # Локальная разработка
├── prod.compose.yaml              # Production
├── build.gradle.kts               # Gradle конфигурация
├── gradlew / gradlew.bat          # Gradle Wrapper
├── check-requirements.sh          # Проверка требований
├── README.md                      # Главная документация
├── REQUIREMENTS.md                # Системные требования
├── QUICKSTART.md                  # Быстрый старт
├── MAX_HACKATHON.md              # Документация для хакатона
├── CHECKLIST.md                   # Чеклист проверки
├── FINAL_REPORT.md               # Итоговый отчет
└── INDEX.md                       # Этот файл
```

## Быстрая навигация

**Хочу быстро запустить проект:**
→ QUICKSTART.md

**Хочу понять что делает проект:**
→ MAX_HACKATHON.md

**Нужна полная документация:**
→ README.md

**Проверить готовность системы:**
→ CHECKLIST.md или `./check-requirements.sh`

**Настроить CI/CD:**
→ .github/workflows/docker-build.yml

**Интегрировать фронтенд:**
→ frontend/README.md

## Ключевые команды

```bash
# Проверка требований
./check-requirements.sh

# Локальная разработка
docker-compose up -d
./gradlew bootRun

# Production запуск
docker-compose -f prod.compose.yaml up -d --build

# Проверка здоровья
curl http://localhost:8080/actuator/health

# Логи
docker logs unihub-backend -f

# Остановка
docker-compose down
```

## Полезные ссылки после запуска

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- MinIO Console: http://localhost:9001
- PostgreSQL: localhost:5432

## Контакты

Репозиторий: <repository-url>
Issues: <repository-url>/issues

---

**Все материалы готовы для демонстрации на хакатоне MAX!**
