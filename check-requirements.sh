#!/bin/bash

# Скрипт проверки системных требований для UniHub Backend
# Использование: ./check-requirements.sh

set -e

echo "╔════════════════════════════════════════════════════════════╗"
echo "║   Проверка системных требований UniHub Backend            ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

# Цвета для вывода
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

ERRORS=0
WARNINGS=0

# Функция для проверки команды
check_command() {
    if command -v $1 &> /dev/null; then
        VERSION=$($2 2>&1 | head -n 1)
        echo -e "${GREEN}${NC} $3: ${VERSION}"
        return 0
    else
        echo -e "${RED}${NC} $3 не установлен"
        ERRORS=$((ERRORS + 1))
        return 1
    fi
}

# Функция для проверки версии
check_version() {
    CURRENT=$1
    REQUIRED=$2
    NAME=$3

    if [ "$(printf '%s\n' "$REQUIRED" "$CURRENT" | sort -V | head -n1)" = "$REQUIRED" ]; then
        echo -e "${GREEN}${NC} $NAME: $CURRENT (требуется >= $REQUIRED)"
        return 0
    else
        echo -e "${YELLOW}${NC} $NAME: $CURRENT (рекомендуется >= $REQUIRED)"
        WARNINGS=$((WARNINGS + 1))
        return 1
    fi
}

# Функция для проверки порта
check_port() {
    PORT=$1
    NAME=$2

    if lsof -Pi :$PORT -sTCP:LISTEN -t >/dev/null 2>&1 ; then
        PROCESS=$(lsof -Pi :$PORT -sTCP:LISTEN | tail -n 1 | awk '{print $1}')
        echo -e "${RED}${NC} Порт $PORT ($NAME) занят процессом: $PROCESS"
        ERRORS=$((ERRORS + 1))
        return 1
    else
        echo -e "${GREEN}${NC} Порт $PORT ($NAME) свободен"
        return 0
    fi
}

echo "1. Проверка программного обеспечения"
echo "──────────────────────────────────────"

# Проверка Java
if check_command java "java -version" "Java"; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    check_version "$JAVA_VERSION" "21" "Java версия"
fi

# Проверка Docker
if check_command docker "docker --version" "Docker"; then
    DOCKER_VERSION=$(docker --version | grep -oE '[0-9]+\.[0-9]+' | head -1)
    check_version "$DOCKER_VERSION" "24.0" "Docker версия"
fi

# Проверка Docker Compose
if check_command docker-compose "docker-compose --version" "Docker Compose"; then
    COMPOSE_VERSION=$(docker-compose --version | grep -oE '[0-9]+\.[0-9]+' | head -1)
    check_version "$COMPOSE_VERSION" "2.20" "Docker Compose версия"
fi

# Проверка Git (опционально)
check_command git "git --version" "Git" || true

echo ""
echo "2. Проверка доступности портов"
echo "──────────────────────────────────────"

check_port 8080 "Backend API"
check_port 5432 "PostgreSQL"
check_port 9000 "MinIO API"
check_port 9001 "MinIO Console"

echo ""
echo "3. Проверка системных ресурсов"
echo "──────────────────────────────────────"

# Проверка RAM
if [[ "$OSTYPE" == "darwin"* ]]; then
    TOTAL_RAM=$(sysctl -n hw.memsize | awk '{print $1/1024/1024/1024}')
else
    TOTAL_RAM=$(free -g | awk '/^Mem:/{print $2}')
fi

if (( $(echo "$TOTAL_RAM >= 4" | bc -l) )); then
    echo -e "${GREEN}${NC} ОЗУ: ${TOTAL_RAM}GB (требуется >= 4GB)"
else
    echo -e "${YELLOW}${NC} ОЗУ: ${TOTAL_RAM}GB (рекомендуется >= 4GB)"
    WARNINGS=$((WARNINGS + 1))
fi

# Проверка дискового пространства
AVAILABLE_SPACE=$(df -h . | awk 'NR==2 {print $4}')
AVAILABLE_GB=$(df -BG . | awk 'NR==2 {print $4}' | tr -d 'G')

if (( AVAILABLE_GB >= 5 )); then
    echo -e "${GREEN}${NC} Дисковое пространство: $AVAILABLE_SPACE (требуется >= 5GB)"
else
    echo -e "${RED}${NC} Дисковое пространство: $AVAILABLE_SPACE (требуется >= 5GB)"
    ERRORS=$((ERRORS + 1))
fi

# Проверка CPU
if [[ "$OSTYPE" == "darwin"* ]]; then
    CPU_CORES=$(sysctl -n hw.ncpu)
else
    CPU_CORES=$(nproc)
fi

if (( CPU_CORES >= 2 )); then
    echo -e "${GREEN}${NC} CPU ядер: $CPU_CORES (требуется >= 2)"
else
    echo -e "${YELLOW}${NC} CPU ядер: $CPU_CORES (рекомендуется >= 2)"
    WARNINGS=$((WARNINGS + 1))
fi

echo ""
echo "4. Проверка Docker состояния"
echo "──────────────────────────────────────"

if command -v docker &> /dev/null; then
    if docker info &> /dev/null; then
        echo -e "${GREEN}${NC} Docker daemon работает"

        # Проверка запущенных контейнеров
        RUNNING=$(docker ps -q | wc -l | tr -d ' ')
        echo -e "${GREEN}${NC} Запущено контейнеров: $RUNNING"
    else
        echo -e "${RED}${NC} Docker daemon не запущен"
        echo "   Запустите: sudo systemctl start docker (Linux) или Docker Desktop (macOS/Windows)"
        ERRORS=$((ERRORS + 1))
    fi
fi

echo ""
echo "╔════════════════════════════════════════════════════════════╗"
echo "║                      ИТОГИ ПРОВЕРКИ                        ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

if [ $ERRORS -eq 0 ] && [ $WARNINGS -eq 0 ]; then
    echo -e "${GREEN}Все проверки пройдены успешно!${NC}"
    echo ""
    echo "Система готова для запуска UniHub Backend."
    echo "Следуйте инструкциям в QUICKSTART.md для запуска проекта."
    exit 0
elif [ $ERRORS -eq 0 ]; then
    echo -e "${YELLOW}Проверка завершена с предупреждениями: $WARNINGS${NC}"
    echo ""
    echo "Система готова для запуска, но рекомендуется устранить предупреждения."
    echo "См. REQUIREMENTS.md для установки рекомендуемых версий."
    exit 0
else
    echo -e "${RED}Обнаружены критические ошибки: $ERRORS${NC}"
    if [ $WARNINGS -gt 0 ]; then
        echo -e "${YELLOW}Предупреждения: $WARNINGS${NC}"
    fi
    echo ""
    echo "Необходимо устранить ошибки перед запуском."
    echo "См. REQUIREMENTS.md для инструкций по установке."
    exit 1
fi
