import type { News, Club, Project, Schedule, Internship, Certificate } from './types-all';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api/student';

// Утилита для обработки ответов
const handleResponse = async <T>(response: Response): Promise<T> => {
  if (!response.ok) {
    const error = await response.json().catch(() => ({ message: 'Network error' }));
    throw new Error(error.message || `HTTP error! status: ${response.status}`);
  }
  return response.json();
};

// Утилита для создания заголовков с токеном
const getHeaders = (): HeadersInit => {
  const token = localStorage.getItem('authToken'); // Или откуда вы берете токен
  return {
    'Content-Type': 'application/json',
    ...(token && { 'Authorization': `Bearer ${token}` }),
  };
};

// ============ NEWS SERVICE ============
export const newsService = {
  getAll: async (): Promise<News[]> => {
    const response = await fetch(`${API_BASE_URL}/news`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse<News[]>(response);
  },

  getById: async (id: number): Promise<News | undefined> => {
    const response = await fetch(`${API_BASE_URL}/news/${id}`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse<News>(response);
  },

  // Создание новостей доступно только для админов,
  // но оставляем метод для совместимости
  create: async (news: Omit<News, 'id' | 'createdAt'>): Promise<News> => {
    const response = await fetch(`${API_BASE_URL}/news`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify(news),
    });
    return handleResponse<News>(response);
  },
};

// ============ CLUBS SERVICE ============
export const clubsService = {
  getAll: async (): Promise<Club[]> => {
    const response = await fetch(`${API_BASE_URL}/clubs`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse<Club[]>(response);
  },

  getById: async (id: number): Promise<Club | undefined> => {
    const response = await fetch(`${API_BASE_URL}/clubs/${id}`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse<Club>(response);
  },

  create: async (club: Omit<Club, 'id' | 'members'>): Promise<Club> => {
    const response = await fetch(`${API_BASE_URL}/clubs`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify(club),
    });
    return handleResponse<Club>(response);
  },
};

// ============ PROJECTS SERVICE ============
export const projectsService = {
  getAll: async (): Promise<Project[]> => {
    const response = await fetch(`${API_BASE_URL}/projects`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse<Project[]>(response);
  },

  getById: async (id: number): Promise<Project | undefined> => {
    const response = await fetch(`${API_BASE_URL}/projects/${id}`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse<Project>(response);
  },

  create: async (project: Omit<Project, 'id' | 'createdAt'>): Promise<Project> => {
    const response = await fetch(`${API_BASE_URL}/projects`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify(project),
    });
    return handleResponse<Project>(response);
  },
};

// ============ SCHEDULE SERVICE ============
export const scheduleService = {
  getAll: async (): Promise<Schedule[]> => {
    const response = await fetch(`${API_BASE_URL}/schedule`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse<Schedule[]>(response);
  },
};

// ============ INTERNSHIPS SERVICE ============
export const internshipsService = {
  getAll: async (): Promise<Internship[]> => {
    const response = await fetch(`${API_BASE_URL}/internships`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse<Internship[]>(response);
  },
};

// ============ CERTIFICATES SERVICE ============
export const certificateService = {
  getAll: async (): Promise<Certificate[]> => {
    const response = await fetch(`${API_BASE_URL}/certificates`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse<Certificate[]>(response);
  },

  create: async (cert: Omit<Certificate, 'id'>): Promise<Certificate> => {
    const response = await fetch(`${API_BASE_URL}/certificates`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify({
        type: cert.type === 'Справка об обучении' ? 'STUDY_CERTIFICATE' : 'STUDY_CERTIFICATE',
        comment: null,
      }),
    });
    return handleResponse<Certificate>(response);
  },
};
