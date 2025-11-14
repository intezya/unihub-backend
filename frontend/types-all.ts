export interface User {
    id: number;
    name: string;
    email: string;
    role: 'student' | 'teacher' | 'staff';
    avatar?: string;
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

export interface Project {
  id: number;
  title: string;
  description: string;
  author: string;
  status: 'Активен' | 'Набор' | 'Завершен';
  category: string;
  createdAt: string;
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
