# Requirements для UniHub Backend

## Системные требования

### Минимальные требования для разработки

- **CPU**: 2 ядра
- **RAM**: 4 GB
- **Дисковое пространство**: 5 GB
- **ОС**: Linux, macOS, Windows (с WSL2)

### Рекомендуемые требования для разработки

- **CPU**: 4 ядра
- **RAM**: 8 GB
- **Дисковое пространство**: 10 GB
- **ОС**: Linux, macOS

### Требования для продакшн

- **CPU**: 2-4 ядра
- **RAM**: 4-8 GB
- **Дисковое пространство**: 20 GB (зависит от объема данных)
- **ОС**: Linux (Ubuntu 22.04 LTS или новее)

## Программное обеспечение

### Обязательные зависимости

#### Для локальной разработки

1. **Java Development Kit (JDK)**
    - Версия: 21 или выше
    - Рек��мендуется: Eclipse Temurin, BellSoft Liberica, или Oracle JDK
    - Проверка: `java -version`

2. **Docker**
    - Версия: 24.0 или выше
    - Проверка: `docker --version`
    - Установка: https://docs.docker.com/get-docker/

3. **Docker Compose**
    - Версия: 2.20 или выше
    - Проверка: `docker-compose --version`
    - Обычно входит в состав Docker Desktop

#### Для продакшн деплоя

1. **Docker**
    - Версия: 24.0 или выше

2. **Docker Compose**
    - Версия: 2.20 или выше

### Опциональные инструменты для разработки

1. **IntelliJ IDEA**
    - Версия: 2023.3 или новее
    - Плагины: Kotlin, Spring Boot, Docker

2. **Git**
    - Версия: 2.40 или выше
    - Для работы с репозиторием

3. **curl или Postman**
    - Для тестирования API

4. **PostgreSQL Client (psql)**
    - Для прямого доступа к базе данных
    - Установка: `brew install postgresql` (macOS) или `apt install postgresql-client` (Ubuntu)

5. **MinIO Client (mc)**
    - Для управления файловым хранилищем
    - Установка: https://min.io/docs/minio/linux/reference/minio-mc.html

## Зависимости приложения (управляются Gradle)

### Runtime зависимости

- **Spring Boot**: 3.5.7
    - spring-boot-starter-web
    - spring-boot-starter-data-jpa
    - spring-boot-starter-security
    - spring-boot-starter-validation

- **Kotlin**: 2.2.20
    - kotlin-reflect
    - kotlin-stdlib

- **База данных**
    - PostgreSQL Driver: latest
    - Liquibase Core: latest

- **Файловое хранилище**
    - MinIO Java Client: 8.5.7

- **Документация API**
    - SpringDoc OpenAPI: 2.8.0

### Development зависимости

- spring-boot-devtools
- spring-boot-docker-compose

### Test зависимости

- spring-boot-starter-test
- kotlin-test-junit5
- spring-security-test
- junit-platform-launcher

## Инфраструктурные зависимости

### PostgreSQL

- **Версия**: 17-alpine
- **Порт**: 5432
- **База данных**: unihub_db
- **Пользователь**: unihub_user (настраивается)
- **Память**: минимум 256 MB, рекомендуется 512 MB

### MinIO

- **Версия**: latest
- **Порт API**: 9000
- **Порт Console**: 9001
- **Память**: минимум 512 MB, рекомендуется 1 GB
- **Дисковое пространство**: зависит от объема загружаемых файлов

## Сетевые требования

### Порты для локальной разработки

- **8080**: Backend API
- **5432**: PostgreSQL
- **9000**: MinIO API
- **9001**: MinIO Console

Убедитесь, что эти порты свободны:

```bash
# Linux/macOS
lsof -i :8080
lsof -i :5432
lsof -i :9000
lsof -i :9001

# Windows
netstat -ano | findstr :8080
netstat -ano | findstr :5432
```

### Продакшн

- **443**: HTTPS (через Traefik)
- **80**: HTTP redirect
- Внутренние порты контейнеров изолированы через Docker networks

## Проверка готовности системы

### Скрипт проверки для Linux/macOS

```bash
#!/bin/bash

echo "Проверка системных требований UniHub Backend"
echo "=============================================="

# Проверка Java
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)
    echo "✓ Java: $JAVA_VERSION"
else
    echo "✗ Java не установлен"
fi

# Проверка Docker
if command -v docker &> /dev/null; then
    DOCKER_VERSION=$(docker --version | cut -d' ' -f3 | tr -d ',')
    echo "✓ Docker: $DOCKER_VERSION"
else
    echo "✗ Docker не установлен"
fi

# Проверка Docker Compose
if command -v docker-compose &> /dev/null; then
    COMPOSE_VERSION=$(docker-compose --version | cut -d' ' -f4 | tr -d ',')
    echo "✓ Docker Compose: $COMPOSE_VERSION"
else
    echo "✗ Docker Compose не установлен"
fi

# Проверка свободных портов
echo ""
echo "П��оверка портов:"
for port in 8080 5432 9000 9001; do
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null ; then
        echo "✗ Порт $port занят"
    else
        echo "✓ Порт $port свободен"
    fi
done

# Проверка дискового пространства
AVAILABLE_SPACE=$(df -h . | awk 'NR==2 {print $4}')
echo ""
echo "Доступное дисковое пространство: $AVAILABLE_SPACE"

echo ""
echo "Проверка завершена"
```

Сохраните как `check-requirements.sh` и запустите:

```bash
chmod +x check-requirements.sh
./check-requirements.sh
```

## Установка зависимостей

### macOS (через Homebrew)

```bash
# JDK
brew install openjdk@21

# Docker Desktop
brew install --cask docker

# Опционально: PostgreSQL client
brew install postgresql

# Опционально: MinIO client
brew install minio/stable/mc
```

### Ubuntu/Debian

```bash
# JDK
sudo apt update
sudo apt install openjdk-21-jdk

# Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Docker Compose
sudo apt install docker-compose-plugin

# Опционально: PostgreSQL client
sudo apt install postgresql-client

# Опционально: MinIO client
wget https://dl.min.io/client/mc/release/linux-amd64/mc
chmod +x mc
sudo mv mc /usr/local/bin/
```

### Windows (через Chocolatey)

```powershell
# JDK
choco install openjdk21

# Docker Desktop
choco install docker-desktop

# Git (если не установлен)
choco install git
```

## Переменные окружения

Создайте файл `.env` в корне проекта:

```env
# База данных
POSTGRES_DB=unihub_db
POSTGRES_USER=unihub_user
POSTGRES_PASSWORD=change_me_in_production

# MinIO
MINIO_ACCESS_KEY=minioadmin
MINIO_SECRET_KEY=change_me_in_production
MINIO_BUCKET=unihub

# Spring
SPRING_PROFILES_ACTIVE=development

# Опционально: SMSC для отправки SMS
SMSC_APIKEY=your_api_key_here
```

## Проверка установки

После установки всех зависимостей:

1. Клонируйте репозиторий:

```bash
git clone <repository-url>
cd backend
```

2. Запустите инфраструктуру:

```bash
docker-compose up -d
```

3. Дождитесь готовности (около 30 секунд):

```bash
docker-compose ps
```

4. Соберите проект:

```bash
./gradlew build -x test
```

5. Запустите приложение:

```bash
./gradlew bootRun
```

6. Проверьте доступность:

```bash
curl http://localhost:8080/actuator/health
```

Ожидаемый ответ: `{"status":"UP"}`

## Поддержка

Если у вас возникли проблемы с установкой зависимостей, создайте issue в репозитории с описанием:

- Операционная система и версия
- Версии установленного ПО
- Текст ошибки
- Логи выполнения команд

## Обновление зависимостей

### Обновление Java зависимостей

```bash
./gradlew dependencyUpdates
```

### Обновление Docker образов

```bash
docker-compose pull
docker-compose -f prod.compose.yaml pull
```

### Обновление Gradle Wrapper

```bash
./gradlew wrapper --gradle-version=8.11
```
