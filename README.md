# UniHub Backend

Бэкенд для платформы UniHub - единая цифровая среда для студентов университетов.

## Описание

UniHub предоставляет студентам доступ к:

- Новостям университета
- Расписанию занятий
- Клубам и сообществам
- Проектам и стажировкам
- Справкам и документам

Система построена на Spring Boot с использованием PostgreSQL для хранения данных и MinIO для файлового хранилища.

## Технологический стек

- **Язык**: Kotlin 2.2.20
- **Фреймворк**: Spring Boot 3.5.7
- **База данных**: PostgreSQL 17
- **Файловое хранилище**: MinIO
- **Контейнеризация**: Docker & Docker Compose
- **Документация API**: OpenAPI/Swagger

## Требования для запуска

### Локальная разработка

- Java 21 или выше
- Docker и Docker Compose
- Gradle (встроен в проект через Gradle Wrapper)

### Продакшн

- Docker и Docker Compose

## Быстрый старт

### Вариант 1: Локальная разработка

1. Клонируйте репозиторий:

```bash
git clone <repository-url>
cd backend
```

2. Создайте файл `.env` в корне проекта:

```env
POSTGRES_DB=unihub_db
POSTGRES_USER=unihub_user
POSTGRES_PASSWORD=unihub_password
MINIO_ACCESS_KEY=minioadmin
MINIO_SECRET_KEY=minioadmin
```

3. Запустите инфраструктуру (PostgreSQL и MinIO):

```bash
docker-compose up -d
```

4. Дождитесь готовности сервисов (около 30 секунд):

```bash
docker-compose ps
```

5. Создайте bucket в MinIO:

Откройте MinIO Console: http://localhost:9001

- Логин: minioadmin
- Пароль: minioadmin

Создайте bucket с именем `unihub` или используйте MinIO Client:

```bash
docker run --rm --network=host minio/mc alias set local http://localhost:9000 minioadmin minioadmin
docker run --rm --network=host minio/mc mb local/unihub
docker run --rm --network=host minio/mc anonymous set public local/unihub
```

6. Запустите приложение:

```bash
./gradlew bootRun
```

7. Проверьте работу:

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/api-docs

### Вариант 2: Docker (полная сборка)

1. Клонируйте репозиторий:

```bash
git clone <repository-url>
cd backend
```

2. Создайте файл `.env` (опционально, используются значения по умолчанию):

```env
POSTGRES_DB=unihub_db
POSTGRES_USER=unihub_user
POSTGRES_PASSWORD=unihub_password
MINIO_ACCESS_KEY=minioadmin
MINIO_SECRET_KEY=minioadmin
SPRING_PROFILES_ACTIVE=production
```

3. Соберите и запустите все сервисы:

```bash
docker-compose -f prod.compose.yaml up -d --build
```

4. Проверьте статус контейнеров:

```bash
docker-compose -f prod.compose.yaml ps
```

5. Проверьте логи:

```bash
docker-compose -f prod.compose.yaml logs -f backend
```

6. Создайте bucket в MinIO (первый запуск):

```bash
docker exec -it unihub-minio mc alias set local http://localhost:9000 minioadmin minioadmin
docker exec -it unihub-minio mc mb local/unihub
docker exec -it unihub-minio mc anonymous set public local/unihub
```

### Вариант 3: Готовый образ из GHCR

```bash
# Скачать образ
docker pull ghcr.io/<owner>/backend:latest

# Запустить с prod.compose.yaml
docker-compose -f prod.compose.yaml up -d
```

## Проверка работоспособности

### Healthcheck

```bash
curl http://localhost:8080/actuator/health
```

Ожидаемый ответ:

```json
{
  "status": "UP"
}
```

### Проверка API

```bash
# Получить список новостей (требуется авторизация)
curl -H "Authorization: Bearer <token>" http://localhost:8080/api/student/news
```

### Проверка базы данных

```bash
docker exec -it unihub-postgres psql -U unihub_user -d unihub_db -c "\dt"
```

### Проверка MinIO

Откройте http://localhost:9001 и авторизуйтесь с `minioadmin`/`minioadmin`

## Доступные сервисы

После запуска доступны следующие сервисы:

| Сервис        | URL                                   | Описание                 |
|---------------|---------------------------------------|--------------------------|
| Backend API   | http://localhost:8080                 | REST API                 |
| Swagger UI    | http://localhost:8080/swagger-ui.html | Документация API         |
| PostgreSQL    | localhost:5432                        | База данных              |
| MinIO API     | http://localhost:9000                 | S3-совместимое хранилище |
| MinIO Console | http://localhost:9001                 | Веб-интерфейс MinIO      |

## Структура проекта

```
backend/
├── src/
│   ├── main/
│   │   ├── kotlin/com/intezya/unihub/
│   │   │   ├── api/          # Контроллеры и DTO
│   │   │   ├── domain/       # Entity и репозитории
│   │   │   ├── service/      # Бизнес-логика
│   │   │   ├── security/     # Безопасность и аутентификация
│   │   │   └── config/       # Конфигурация
│   │   └── resources/
│   │       ├── application.yaml
│   │       └── db/changelog/ # Liquibase миграции
│   └── test/
├── Dockerfile                 # Многоступенчатая сборка
├── compose.yaml              # Локальная разработка
├── prod.compose.yaml         # Production
└── .github/workflows/        # CI/CD
```

## Разработка

### Запуск тестов

```bash
./gradlew test
```

### Сборка без тестов

```bash
./gradlew build -x test
```

### Форматирование кода

```bash
./gradlew ktlintFormat
```

### Проверка стиля кода

```bash
./gradlew ktlintCheck
```

## CI/CD

Проект использует GitHub Actions для автоматической сборки Docker образов.

### Триггеры сборки:

- Push тега вида `v*.*.*` (например, `v1.0.0`)
- Ручной запуск через workflow_dispatch
- Pull request в ветки main/dev (без публикации)

### Ручной запуск сборки:

1. Перейдите в раздел Actions в GitHub
2. Выберите workflow "Build and Push Docker Image"
3. Нажмите "Run workflow"
4. Укажите тег (по умолчанию `latest`)

### Создание релиза:

```bash
git tag -a v1.0.0 -m "Release v1.0.0"
git push origin v1.0.0
```

Образ будет опубликован в `ghcr.io/<owner>/backend:v1.0.0`

## Переменные окружения

| Переменная             | Описание         | Значение по умолчанию |
|------------------------|------------------|-----------------------|
| POSTGRES_HOST          | Хост PostgreSQL  | postgres              |
| POSTGRES_PORT          | Порт PostgreSQL  | 5432                  |
| POSTGRES_DB            | Имя базы данных  | unihub_db             |
| POSTGRES_USER          | Пользователь БД  | unihub_user           |
| POSTGRES_PASSWORD      | Пароль БД        | unihub_password       |
| MINIO_ENDPOINT         | URL MinIO        | http://minio:9000     |
| MINIO_ACCESS_KEY       | Access Key MinIO | minioadmin            |
| MINIO_SECRET_KEY       | Secret Key MinIO | minioadmin            |
| MINIO_BUCKET           | Имя bucket       | unihub                |
| SPRING_PROFILES_ACTIVE | Spring профиль   | production            |

## Остановка сервисов

### Локальная разработка

```bash
# Остановить без удаления данных
docker-compose down

# Остановить с удалением volumes
docker-compose down -v
```

### Production

```bash
# Остановить
docker-compose -f prod.compose.yaml down

# Остановить и удалить volumes
docker-compose -f prod.compose.yaml down -v
```

## Troubleshooting

### Проблема: Порты заняты

Проверьте занятые порты:

```bash
lsof -i :8080
lsof -i :5432
lsof -i :9000
```

Остановите конфликтующие сервисы или измените порты в compose.yaml

### Проблема: База данных не доступна

```bash
# Проверьте логи PostgreSQL
docker logs unihub-postgres

# Проверьте healthcheck
docker inspect unihub-postgres | grep Health -A 10
```

### Проблема: MinIO bucket не существует

```bash
# Создайте bucket вручную
docker exec -it unihub-minio mc alias set local http://localhost:9000 minioadmin minioadmin
docker exec -it unihub-minio mc mb local/unihub
```

### Проблема: Приложение не запускается

```bash
# Проверьте логи
docker logs unihub-backend -f

# Проверьте переменные окружения
docker exec unihub-backend env | grep -E "POSTGRES|MINIO"
```

## API Документация

Полная документация API доступна через Swagger UI после запуска приложения:
http://localhost:8080/swagger-ui.html

Основные эндпоинты:

- `GET /api/student/news` - список новостей
- `GET /api/student/clubs` - список клубов
- `GET /api/student/projects` - список проектов
- `GET /api/student/schedule` - расписание
- `GET /api/student/internships` - стажировки
- `GET /api/student/certificates` - справки

## Лицензия

Proprietary

## Контакты

Для вопросов и предложений создавайте issue в репозитории проекта.

Для удаления данных:

```bash
docker-compose down -v
```
