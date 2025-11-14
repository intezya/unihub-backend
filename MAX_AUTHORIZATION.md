# MAX Bridge Authorization

## Описание

Авторизация через MAX Bridge с использованием JWT токенов.

## Эндпоинты

### POST /auth/max

Авторизация пользователя через MAX Bridge.

**Request Body:**

```json
{
  "initData": "query_id=...&user={\"id\":123,...}&hash=..."
}
```

**Response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "maxUserId": 123456789
}
```

### GET /api/user/me

Получение информации о текущем пользователе.

**Headers:**

```
Authorization: Bearer <JWT_TOKEN>
```

**Response:**

```json
{
  "id": 123,
  "name": "Иван Иванов",
  "role": "student",
  "avatar": "https://example.com/avatar.jpg"
}
```

### GET /api/user/next-event

Получение ближайшего события для пользователя.

**Headers:**

```
Authorization: Bearer <JWT_TOKEN>
```

**Response:**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "title": "Математический анализ",
  "startTime": "10:00",
  "endTime": "11:30",
  "location": "Аудитория 203",
  "type": "Лекция"
}
```

## Конфигурация

В `application.yaml` или `.env` файле необходимо указать:

```yaml
max:
  bot:
    token: ${MAX_BOT_TOKEN}  # Токен бота MAX

jwt:
  secret: ${JWT_SECRET}      # Секретный ключ для JWT (минимум 256 бит)
  expiration: 86400000       # Время жизни токена в миллисекундах (по умолчанию 24 часа)
```

Или в `.env`:

```
MAX_BOT_TOKEN=your_max_bot_token_here
JWT_SECRET=your_jwt_secret_key_min_256_bits_long
```

## Пример использования на фронтенде

```typescript
// Получение initData из MAX WebApp
const initData = window.WebApp.initData;

// Авторизация
const response = await fetch('/auth/max', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({ initData }),
});

const { token } = await response.json();

// Сохранение токена
localStorage.setItem('jwt_token', token);

// Использование токена для запросов
const userResponse = await fetch('/api/user/me', {
  headers: {
    'Authorization': `Bearer ${token}`,
  },
});

const user = await userResponse.json();
console.log(user);
```

## Как работает валидация initData

1. Фронтенд отправляет `initData` строку на `/auth/max`
2. Backend парсит параметры из `initData`
3. Формируется `data_check_string` из всех параметров кроме `hash`
4. Вычисляется `secret_key = SHA256(botToken)`
5. Вычисляется `calculated_hash = HMAC_SHA256(data_check_string, secret_key)`
6. Сравнивается `calculated_hash` с `hash` из `initData`
7. Если хэши совпадают - пользователь авторизован
8. Создается или находится пользователь в БД по `maxUserId`
9. Генерируется JWT токен с данными пользователя
10. Токен возвращается клиенту

## Безопасность

- JWT токены подписаны секретным ключом
- `initData` валидируется через HMAC-SHA256 с использованием токена бота
- Токены имеют ограниченный срок жизни (по умолчанию 24 часа)
- Старая авторизация через cookie остается для совместимости

## Миграция БД

Автоматически создается поле `max_user_id` в таблице `users` через Liquibase:

```sql
ALTER TABLE users
    ADD COLUMN max_user_id bigint NULL,
ADD CONSTRAINT unique_max_user_id UNIQUE (max_user_id);
```
