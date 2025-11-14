export interface User {
    id: number;
    name: string;
    email: string;
    role: 'student' | 'teacher' | 'staff';
    avatar?: string;
}

// ============ МЕРОПРИЯТИЯ ============
export interface Event {
  id: number;
  title: string;
  description: string;
  image?: string;
  date: string;
  location?: string;
  maxParticipants?: number;
  currentParticipants: number;
  createdAt: string;
  createdBy: string;
}

export interface EventRegistration {
  id: number;
  eventId: number;
  studentName: string;
  studentGroup: string;
  studentNumber: string;
  registeredAt: string;
}

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

// ============ ПРОЕКТЫ ============
export interface Project {
  id: number;
  title: string;
  description: string;
  author: string;
  authorId: number;
  status: 'Активен' | 'Набор' | 'Завершен';
  category: string;
  createdAt: string;
  lookingFor?: string; // Кого ищут в команду
  contactInfo?: string;
}

export interface ProjectApplication {
  id: number;
  projectId: number;
  applicantName: string;
  applicantEmail: string;
  experience: string;
  appliedAt: string;
  status: 'Новая' | 'Рассмотрена' | 'Принята' | 'Отклонена';
}

export interface Student {
  id: number;
  name: string;
  email: string;
  photo?: string;
  course: number;
  group: string;
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

// ============ СТАЖИРОВКИ ============
export interface Internship {
  id: number;
  company: string;
  position: string;
  description: string;
  duration: string;
  salary?: string;
  deadline: string;
  logo?: string; // Логотип компании
  status: 'Активна' | 'Закрыта' | 'Завершена';
  externalUrl?: string; // Ссылка на сайт компании
  direction?: string; // Направление (IT, Маркетинг и т.д.)
}

export interface Certificate {
  id: number;
  type: string;
  status: 'Готова' | 'В обработке' | 'Отклонена';
  requestDate: string;
  issueDate?: string;
}
