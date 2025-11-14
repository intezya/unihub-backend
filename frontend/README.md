# UniHub Frontend Integration Guide

## Готовые файлы для интеграции

### 📁 Структура файлов

```
frontend/
├── types-all.ts           # TypeScript интерфейсы (уже готовы)
├── api-services.ts        # Моки для разработки (старая версия)
├── api-services-ready.ts  # ✨ ГОТОВЫЕ API сервисы с реальными запросами
└── API_DOCUMENTATION.md   # Документация по всем эндпоинтам
```

---

## 🚀 Быстрый старт

### Шаг 1: Скопируйте файлы в проект

```bash
cp frontend/types-all.ts src/types/
cp frontend/api-services-ready.ts src/services/api-services.ts
```

### Шаг 2: Настройте переменные окружения

Создайте `.env`:

```env
REACT_APP_API_URL=http://localhost:8080/api/student
```

### Шаг 3: Используйте сервисы

```typescript
import { newsService, clubsService, projectsService } from './services/api-services';

// Получить все новости
const news = await newsService.getAll();

// Получить клубы
const clubs = await clubsService.getAll();

// Получить проекты
const projects = await projectsService.getAll();
```

---

## 📋 Что изменилось в бэкенде

### Созданы новые DTO (Data Transfer Objects)

Все DTO находятся в `com.intezya.unihub.api.dto`:

- ✅ **NewsDto** - адаптирован под фронтенд (id: Long, text вместо content, author)
- ✅ **ClubDto** - добавлены поля members, category, logo
- ✅ **ProjectDto** - добавлены author, status, category, createdAt
- ✅ **ScheduleDto** - format "time", type, date
- ✅ **InternshipDto** - company, position, duration, salary, deadline
- ✅ **CertificateDto** - локализованные статусы и типы

### Обновлены контроллеры

Все контроллеры теперь возвращают данные в формате, ожидаемом фронтендом:

- ✅ **NewsController** - `/api/student/news` + GET `/{id}`
- ✅ **ClubController** - `/api/student/clubs` + GET `/{id}`
- ✅ **ProjectController** - `/api/student/projects` + GET `/{id}`
- ✅ **StudentController** - `/api/student/schedule` (возвращает ScheduleDto[])
- ✅ **InternshipController** - `/api/student/internships`
- ✅ **CertificateController** - `/api/student/certificates` (GET + POST)

---

## 🔑 Аутентификация

Все запросы требуют JWT токен в заголовке:

```typescript
Authorization: Bearer <token>
```

Токен автоматически добавляется из `localStorage`:

```typescript
localStorage.setItem('authToken', 'your-jwt-token');
```

---

## 📡 Доступные сервисы

### newsService

```typescript
newsService.getAll(): Promise<News[]>
newsService.getById(id: number): Promise<News | undefined>
newsService.create(news): Promise<News> // для админов
```

### clubsService

```typescript
clubsService.getAll(): Promise<Club[]>
clubsService.getById(id: number): Promise<Club | undefined>
clubsService.create(club): Promise<Club> // для админов
```

### projectsService

```typescript
projectsService.getAll(): Promise<Project[]>
projectsService.getById(id: number): Promise<Project | undefined>
projectsService.create(project): Promise<Project> // для админов
```

### scheduleService

```typescript
scheduleService.getAll(): Promise<Schedule[]>
```

### internshipsService

```typescript
internshipsService.getAll(): Promise<Internship[]>
```

### certificateService

```typescript
certificateService.getAll(): Promise<Certificate[]>
certificateService.create(cert): Promise<Certificate>
```

---

## 🎨 Маппинг данных

### ID

- Бэкенд использует UUID
- Фронтенд получает Long (через hashCode)

### Даты

- Формат: ISO 8601 (`2024-11-01T10:00:00+03:00`)
- Для отображения используйте: `new Date(dateString).toLocaleString()`

### Статусы справок

| Backend  | Frontend    |
|----------|-------------|
| NEW      | В обработке |
| APPROVED | Готова      |
| REJECTED | Отклонена   |

### Изображения

- Все URL - presigned URLs от MinIO
- Действительны ограниченное время
- Автоматически генерируются бэкендом

---

## 🔧 Обработка ошибок

```typescript
try {
  const news = await newsService.getAll();
} catch (error) {
  console.error('Ошибка загрузки новостей:', error.message);
}
```

---

## 📝 Примеры использования

### React компонент с новостями

```typescript
import { useEffect, useState } from 'react';
import { newsService } from './services/api-services';
import type { News } from './types/types-all';

export const NewsList = () => {
  const [news, setNews] = useState<News[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadNews = async () => {
      try {
        const data = await newsService.getAll();
        setNews(data);
      } catch (error) {
        console.error('Error loading news:', error);
      } finally {
        setLoading(false);
      }
    };
    
    loadNews();
  }, []);

  if (loading) return <div>Загрузка...</div>;

  return (
    <div>
      {news.map(item => (
        <div key={item.id}>
          <h2>{item.title}</h2>
          <p>{item.text}</p>
          {item.image && <img src={item.image} alt={item.title} />}
          <small>{new Date(item.createdAt).toLocaleString()}</small>
        </div>
      ))}
    </div>
  );
};
```

---

## ✅ Чек-лист интеграции

- [ ] Скопировать `api-services-ready.ts` в проект
- [ ] Настроить `.env` с `REACT_APP_API_URL`
- [ ] Настроить сохранение JWT токена в localStorage
- [ ] Протестировать все эндпоинты
- [ ] Добавить обработку ошибок
- [ ] Добавить индикаторы загрузки

---

## 🐛 Troubleshooting

### CORS ошибки

Убедитесь, что бэкенд настроен для приема запросов с вашего фронтенд домена.

### 401 Unauthorized

Проверьте:

1. Токен сохранен в localStorage
2. Токен не истек
3. Формат заголовка: `Bearer <token>`

### Изображения не загружаются

- Presigned URLs имеют ограниченное время жизни
- Переполучите список при истечении

---

## 📞 Контакты

Если возникли вопросы по интеграции, проверьте:

1. `API_DOCUMENTATION.md` - полная документация по API
2. Backend Swagger: `http://localhost:8080/swagger-ui.html` (если настроен)

---

**Готово! Все API эндпоинты готовы к использованию. Просто скопируйте файлы и начните разработку! 🎉**
