# Чеклист для запуска UniHub Backend

Используйте этот чеклист для проверки готовности системы к демонстрации на хакатоне MAX.

## Предварительная проверка

- [ ] Установлен Docker (версия 24.0+)
  ```bash
  docker --version
  ```

- [ ] Установлен Docker Compose (версия 2.20+)
  ```bash
  docker-compose --version
  ```

- [ ] Свободно минимум 5GB места на диске
  ```bash
  df -h .
  ```

- [ ] Свободны порты 8080, 5432, 9000, 9001
  ```bash
  # Linux/macOS
  lsof -i :8080 && lsof -i :5432 && lsof -i :9000 && lsof -i :9001
  
  # Или запустите
  ./check-requirements.sh
  ```

## Запуск проекта

- [ ] Клонирован репозиторий
  ```bash
  git clone <repository-url>
  cd backend
  ```

- [ ] Создан файл .env
  ```bash
  cat > .env << 'EOF'
  POSTGRES_DB=unihub_db
  POSTGRES_USER=unihub_user
  POSTGRES_PASSWORD=unihub_pass
  MINIO_ACCESS_KEY=minioadmin
  MINIO_SECRET_KEY=minioadmin123
  EOF
  ```

- [ ] Запущены все сервисы
  ```bash
  docker-compose -f prod.compose.yaml up -d --build
  ```

- [ ] Ожидание готовности (2-3 минуты)
  ```bash
  sleep 120
  docker-compose -f prod.compose.yaml ps
  ```

- [ ] Все контейнеры в статусе "Up"
  ```bash
  docker-compose -f prod.compose.yaml ps | grep "Up"
  ```

## Инициализация MinIO

- [ ] Создан bucket unihub
  ```bash
  docker exec -it unihub-minio mc alias set local http://localhost:9000 minioadmin minioadmin123
  docker exec -it unihub-minio mc mb local/unihub
  docker exec -it unihub-minio mc anonymous set public local/unihub
  ```

- [ ] Bucket доступен
  ```bash
  docker exec -it unihub-minio mc ls local/
  ```

## Проверка работоспособности

- [ ] Backend отвечает на healthcheck
  ```bash
  curl http://localhost:8080/actuator/health
  # Ожидается: {"status":"UP"}
  ```

- [ ] Swagger UI доступен
  ```
  Открыть: http://localhost:8080/swagger-ui.html
  ```

- [ ] MinIO Console доступен
  ```
  Открыть: http://localhost:9001
  Логин: minioadmin
  Пароль: minioadmin123
  ```

- [ ] PostgreSQL работает
  ```bash
  docker exec -it unihub-postgres pg_isready
  # Ожидается: postgres:5432 - accepting connections
  ```

## Демонстрация функциональности

- [ ] Просмотр таблиц базы данных
  ```bash
  docker exec -it unihub-postgres psql -U unihub_user -d unihub_db -c "\dt"
  ```

- [ ] Проверка миграций Liquibase
  ```bash
  docker exec -it unihub-postgres psql -U unihub_user -d unihub_db -c "SELECT * FROM databasechangelog ORDER BY dateexecuted DESC LIMIT 5;"
  ```

- [ ] Логи приложения без ошибок
  ```bash
  docker logs unihub-backend --tail=50
  # Не должно быть ERROR или FATAL
  ```

- [ ] API эндпоинты отвечают
  ```bash
  # Проверка здоровья
  curl http://localhost:8080/actuator/health
  
  # Проверка info
  curl http://localhost:8080/actuator/info
  ```

## Документация

- [ ] README.md содержит полную инструкцию
- [ ] REQUIREMENTS.md описывает все зависимости
- [ ] QUICKSTART.md для быстрого старта
- [ ] MAX_HACKATHON.md для хакатона
- [ ] Dockerfile оптимизирован
- [ ] CI/CD workflow настроен (.github/workflows/docker-build.yml)

## Для GitHub Container Registry

- [ ] Workflow файл создан
  ```bash
  ls -la .github/workflows/docker-build.yml
  ```

- [ ] Можно создать тег для автоматической сборки
  ```bash
  git tag -a v1.0.0 -m "Release v1.0.0"
  git push origin v1.0.0
  ```

- [ ] Или запустить вручную через GitHub Actions UI

## Остановка и очистка

- [ ] Остановка сервисов
  ```bash
  docker-compose -f prod.compose.yaml down
  ```

- [ ] Очистка volumes (если нужен чистый старт)
  ```bash
  docker-compose -f prod.compose.yaml down -v
  ```

- [ ] Удаление образов (опционально)
  ```bash
  docker rmi $(docker images -q 'unihub*')
  ```

## Troubleshooting

Если что-то не работает:

1. **Проверьте логи всех сервисов:**
   ```bash
   docker-compose -f prod.compose.yaml logs
   ```

2. **Проверьте конкретный сервис:**
   ```bash
   docker logs unihub-backend -f
   docker logs unihub-postgres -f
   docker logs unihub-minio -f
   ```

3. **Проверьте статус контейнеров:**
   ```bash
   docker-compose -f prod.compose.yaml ps
   ```

4. **Проверьте сеть:**
   ```bash
   docker network ls
   docker network inspect backend_database
   ```

5. **Пересоздайте сервисы:**
   ```bash
   docker-compose -f prod.compose.yaml down -v
   docker-compose -f prod.compose.yaml up -d --build
   ```

## Финальная проверка перед демонстрацией

- [ ] Все контейнеры запущены и здоровы
- [ ] Backend отвечает на запросы
- [ ] Swagger UI загружается
- [ ] MinIO Console доступен
- [ ] База данных содержит таблицы
- [ ] Логи не содержат критических ошибок
- [ ] Порты доступны извне (если нужно)

## Готово к демонстрации!

Если все пункты отмечены ✓ - проект готов к показу на хакатоне MAX.

Основные URL для демонстрации:

- **API**: http://localhost:8080
- **Swagger**: http://localhost:8080/swagger-ui.html
- **MinIO**: http://localhost:9001

Удачи на хакатоне! 🚀
