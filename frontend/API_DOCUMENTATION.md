# API Documentation для Frontend

## Базовый URL

```
http://localhost:8080/api/student
```

## Аутентификация

Все запросы требуют JWT токен в заголовке:

```
Authorization: Bearer <token>
```

---

## Endpoints

### 1. News (Новости)

#### GET `/api/student/news`

Получить все новости университета

**Response:**

```typescript
News[] // массив объектов News
```

**Пример ответа:**

```json
[
  {
    "id": 1,
    "title": "Начало учебного года",
    "text": "Добро пожаловать в новый учебный год!",
    "image": "https://presigned-url.minio.com/...",
    "createdAt": "2024-09-01T10:00:00+03:00",
    "author": "Администрация"
  }
]
```

#### GET `/api/student/news/{id}`

Получить новость по ID

**Response:**

```typescript
News | null
```

---

### 2. Clubs (Клубы)

#### GET `/api/student/clubs`

Получить все клубы университета

**Response:**

```typescript
Club[] // массив объектов Club
```

**Пример ответа:**

```json
[
  {
    "id": 1,
    "name": "IT клуб",
    "description": "Клуб для программистов и разработчиков",
    "logo": "https://presigned-url.minio.com/...",
    "members": 45,
    "category": "Технологии"
  }
]
```

#### GET `/api/student/clubs/{id}`

Получить клуб по ID

**Response:**

```typescript
Club | null
```

---

### 3. Projects (Проекты)

#### GET `/api/student/projects`

Получить все проекты университета

**Response:**

```typescript
Project[] // массив объектов Project
```

**Пример ответа:**

```json
[
  {
    "id": 1,
    "title": "Разработка мобильного приложения",
    "description": "Нужны React Native разработчики",
    "author": "Автор проекта",
    "status": "Активен",
    "category": "IT",
    "createdAt": "2024-11-01T10:00:00+03:00"
  }
]
```

#### GET `/api/student/projects/{id}`

Получить проект по ID

**Response:**

```typescript
Project | null
```

---

### 4. Schedule (Расписание)

#### GET `/api/student/schedule`

Получить расписание студента

**Response:**

```typescript
Schedule[] // массив объектов Schedule
```

**Пример ответа:**

```json
[
  {
    "id": 1,
    "subject": "Математический анализ",
    "time": "09:00 - 10:30",
    "room": "201",
    "teacher": "Смирнов А.В.",
    "type": "Лекция",
    "date": "2024-11-14"
  }
]
```

---

### 5. Internships (Стажировки)

#### GET `/api/student/internships`

Получить все стажировки

**Response:**

```typescript
Internship[] // массив объектов Internship
```

**Пример ответа:**

```json
[
  {
    "id": 1,
    "company": "Яндекс",
    "position": "Frontend разработчик",
    "description": "Стажировка в команде разработки интерфейсов",
    "duration": "3 месяца",
    "salary": "По договоренности",
    "deadline": "2024-12-01"
  }
]
```

---

### 6. Certificates (Справки)

#### GET `/api/student/certificates`

Получить все справки студента

**Response:**

```typescript
Certificate[] // массив объектов Certificate
```

**Пример ответа:**

```json
[
  {
    "id": 1,
    "type": "Справка об обучении",
    "status": "Готова",
    "requestDate": "2024-11-10",
    "issueDate": "2024-11-12"
  }
]
```

#### POST `/api/student/certificates`

Создать запрос на справку

**Request Body:**

```json
{
  "type": "STUDY_CERTIFICATE",
  "comment": null
}
```

**Response:**

```typescript
Certificate
```

---

## Использование

### Вариант 1: Замена существующих моков

Просто замените содержимое `api-services.ts` на содержимое `api-services-ready.ts`

### Вариант 2: Постепенная миграция

Импортируйте сервисы из готового файла:

```typescript
import { newsService, clubsService } from './api-services-ready';
```

### Настройка переменных окружения

Создайте `.env` файл:

```
REACT_APP_API_URL=http://localhost:8080/api/student
```

### Обработка токена

Токен должен храниться в `localStorage`:

```typescript
localStorage.setItem('authToken', 'your-jwt-token');
```

---

## Типы данных (из types-all.ts)

```typescript
export interface News {
  id: number;
  title: string;
  text: string;
  image?: string;
  createdAt: string;
  author: string;
}

export interface Club {
  id: number;
  name: string;
  description: string;
  logo?: string;
  members: number;
  category: string;
}

export interface Project {
  id: number;
  title: string;
  description: string;
  author: string;
  status: 'Активен' | 'Набор' | 'Завершен';
  category: string;
  createdAt: string;
}

export interface Schedule {
  id: number;
  subject: string;
  time: string;
  room: string;
  teacher: string;
  type: 'Лекция' | 'Практика' | 'Семинар';
  date: string;
}

export interface Internship {
  id: number;
  company: string;
  position: string;
  description: string;
  duration: string;
  salary?: string;
  deadline: string;
}

export interface Certificate {
  id: number;
  type: string;
  status: 'Готова' | 'В обработке' | 'Отклонена';
  requestDate: string;
  issueDate?: string;
}
```

---

## Примечания

1. Все изображения (logo, image) возвращаются как presigned URLs от MinIO с истечением через некоторое время
2. ID в бэкенде - UUID, но для фронтенда конвертируются в Long через hashCode
3. Даты возвращаются в ISO 8601 формате
4. Статусы справок маппятся:
    - NEW → "В обработке"
    - APPROVED → "Готова"
    - REJECTED → "Отклонена"
