# UniHub Backend

## Запуск

### 1. Запуск инфраструктуры (PostgreSQL + MinIO)

```bash
docker-compose up -d
```

### 2. Проверка сервисов

- PostgreSQL: `localhost:5432`
    - Database: `unihub`
    - User: `unihub`
    - Password: `unihub123`

- MinIO:
    - API: `http://localhost:9000`
    - Console: `http://localhost:9001`
    - Access Key: `minioadmin`
    - Secret Key: `minioadmin123`

### 3. Создание bucket в MinIO

После запуска MinIO нужно создать bucket:

```bash
# Используя MinIO Client (mc)
mc alias set local http://localhost:9000 minioadmin minioadmin123
mc mb local/unihub
```

Или через веб-интерфейс: http://localhost:9001

### 4. Запуск приложения

```bash
./gradlew bootRun
```

API будет доступен по адресу: http://localhost:8080

Swagger UI: http://localhost:8080/swagger-ui.html

## Остановка

```bash
docker-compose down
```

Для удаления данных:

```bash
docker-compose down -v
```
