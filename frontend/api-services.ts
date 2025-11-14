import type {News, Club, Project, Schedule, Internship, Certificate, User} from '../model';

// ============ NEWS SERVICE ============
let mockNews: News[] = [
  {
    id: 1,
    title: 'Начало учебного года',
    text: 'Добро пожаловать в новый учебный год!',
    image: 'https://placehold.co/300x200',
    createdAt: '2024-09-01T10:00:00Z',
    author: 'Администрация',
  },
  {
    id: 2,
    title: 'Хакатон 2024',
    text: 'Приглашаем всех на университетский хакатон',
    image: 'https://placehold.co/300x200',
    createdAt: '2024-10-15T14:30:00Z',
    author: 'IT клуб',
  },
];

export const newsService = {
  getAll: async (): Promise<News[]> => {
    return new Promise((resolve) => {
      setTimeout(() => resolve([...mockNews]), 500);
    });
  },
  getById: async (id: number): Promise<News | undefined> => {
    return new Promise((resolve) => {
      setTimeout(() => resolve(mockNews.find((n) => n.id === id)), 300);
    });
  },
  create: async (news: Omit<News, 'id' | 'createdAt'>): Promise<News> => {
    return new Promise((resolve) => {
      setTimeout(() => {
        const newNews: News = {
          ...news,
          id: Date.now(),
          createdAt: new Date().toISOString(),
        };
        mockNews = [newNews, ...mockNews];
        resolve(newNews);
      }, 500);
    });
  },
};

// ============ CLUBS SERVICE ============
let mockClubs: Club[] = [
  {
    id: 1,
    name: 'IT клуб',
    description: 'Клуб для программистов и разработчиков',
    logo: 'https://placehold.co/128x128',
    members: 45,
    category: 'Технологии',
  },
  {
    id: 2,
    name: 'Спортивный клуб',
    description: 'Занимаемся разными видами спорта',
    logo: 'https://placehold.co/128x128',
    members: 120,
    category: 'Спорт',
  },
];

export const clubsService = {
  getAll: async (): Promise<Club[]> => {
    return new Promise((resolve) => {
      setTimeout(() => resolve([...mockClubs]), 500);
    });
  },
  getById: async (id: number): Promise<Club | undefined> => {
    return new Promise((resolve) => {
      setTimeout(() => resolve(mockClubs.find((c) => c.id === id)), 300);
    });
  },
  create: async (club: Omit<Club, 'id' | 'members'>): Promise<Club> => {
    return new Promise((resolve) => {
      setTimeout(() => {
        const newClub: Club = {
          ...club,
          id: Date.now(),
          members: 1,
        };
        mockClubs = [newClub, ...mockClubs];
        resolve(newClub);
      }, 500);
    });
  },
};

// ============ PROJECTS SERVICE ============
let mockProjects: Project[] = [
  {
    id: 1,
    title: 'Разработка мобильного приложения',
    description: 'Нужны React Native разработчики',
    author: 'Иван Иванов',
    status: 'Активен',
    category: 'IT',
    createdAt: '2024-11-01T10:00:00Z',
  },
  {
    id: 2,
    title: 'Исследование в области AI',
    description: 'Ищем людей для участия в ML проекте',
    author: 'Мария Петрова',
    status: 'Набор',
    category: 'Наука',
    createdAt: '2024-11-05T12:00:00Z',
  },
];

export const projectsService = {
  getAll: async (): Promise<Project[]> => {
    return new Promise((resolve) => {
      setTimeout(() => resolve([...mockProjects]), 500);
    });
  },
  getById: async (id: number): Promise<Project | undefined> => {
    return new Promise((resolve) => {
      setTimeout(() => resolve(mockProjects.find((p) => p.id === id)), 300);
    });
  },
  create: async (project: Omit<Project, 'id' | 'createdAt'>): Promise<Project> => {
    return new Promise((resolve) => {
      setTimeout(() => {
        const newProject: Project = {
          ...project,
          id: Date.now(),
          createdAt: new Date().toISOString(),
        };
        mockProjects = [newProject, ...mockProjects];
        resolve(newProject);
      }, 500);
    });
  },
};

// ============ SCHEDULE SERVICE ============
const mockSchedule: Schedule[] = [
  {
    id: 1,
    subject: 'Математический анализ',
    time: '09:00 - 10:30',
    room: '201',
    teacher: 'Смирнов А.В.',
    type: 'Лекция',
    date: '2024-11-14',
  },
  {
    id: 2,
    subject: 'Программирование',
    time: '11:00 - 12:30',
    room: '305',
    teacher: 'Козлова М.И.',
    type: 'Практика',
    date: '2024-11-14',
  },
];

export const scheduleService = {
  getAll: async (): Promise<Schedule[]> => {
    return new Promise((resolve) => {
      setTimeout(() => resolve([...mockSchedule]), 500);
    });
  },
};

// ============ INTERNSHIPS SERVICE ============
const mockInternships: Internship[] = [
  {
    id: 1,
    company: 'Яндекс',
    position: 'Frontend разработчик',
    description: 'Стажировка в команде разработки интерфейсов',
    duration: '3 месяца',
    salary: '50000 руб',
    deadline: '2024-12-01',
  },
  {
    id: 2,
    company: 'VK',
    position: 'Backend разработчик',
    description: 'Работа с высоконагруженными системами',
    duration: '6 месяцев',
    salary: '60000 руб',
    deadline: '2024-11-30',
  },
];

export const internshipsService = {
  getAll: async (): Promise<Internship[]> => {
    return new Promise((resolve) => {
      setTimeout(() => resolve([...mockInternships]), 500);
    });
  },
};

// ============ CERTIFICATES SERVICE ============
let mockCertificates: Certificate[] = [
  {
    id: 1,
    type: 'Справка об обучении',
    status: 'Готова',
    requestDate: '2024-11-10',
    issueDate: '2024-11-12',
  },
  {
    id: 2,
    type: 'Справка с места учебы',
    status: 'В обработке',
    requestDate: '2024-11-13',
    issueDate: undefined,
  },
];

export const certificateService = {
  getAll: async (): Promise<Certificate[]> => {
    return new Promise((resolve) => {
      setTimeout(() => resolve([...mockCertificates]), 500);
    });
  },
  create: async (cert: Omit<Certificate, 'id'>): Promise<Certificate> => {
    return new Promise((resolve) => {
      setTimeout(() => {
        const newCert: Certificate = {
          ...cert,
          id: Date.now(),
        };
        mockCertificates = [newCert, ...mockCertificates];
        resolve(newCert);
      }, 500);
    });
  },
};

const mockUser: User = {
    id: 1,
    name: 'Иван Иванов',
    email: 'ivan.ivanov@university.ru',
    role: 'student',
    avatar: 'https://placehold.co/128x128',
};

export const userService = {
    getCurrentUser: async (): Promise<User> => {
        return new Promise((resolve) => {
            setTimeout(() => resolve({ ...mockUser }), 300);
        });
    },

    setUserRole: async (role: 'student' | 'teacher' | 'staff'): Promise<User> => {
        return new Promise((resolve) => {
            setTimeout(() => {
                mockUser.role = role;
                resolve({ ...mockUser });
            }, 300);
        });
    },
};
