import type { News, Club, Project, Schedule, Internship, Certificate, User } from './types-all';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';
const STUDENT_API_BASE = `${API_BASE_URL}/api/student`;
const USER_API_BASE = `${API_BASE_URL}/api/user`;
const AUTH_API_BASE = `${API_BASE_URL}/auth`;

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
  const token = localStorage.getItem('jwt_token');
  return {
    'Content-Type': 'application/json',
    ...(token && { 'Authorization': `Bearer ${token}` }),
  };
};

// ============ AUTH SERVICE ============
export const authService = {
  authenticateWithMax: async (initData: string): Promise<{ token: string; userId: string; maxUserId: number }> => {
    const response = await fetch(`${AUTH_API_BASE}/max`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ initData }),
    });
    return handleResponse(response);
  },
};

// ============ USER SERVICE ============
export const userService = {
  getMe: async (): Promise<User> => {
    const response = await fetch(`${USER_API_BASE}/me`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse<User>(response);
  },

  getNextEvent: async (): Promise<{
    id: string;
    title: string;
    startTime: string;
    endTime: string;
    location: string;
    type: string;
  } | null> => {
    const response = await fetch(`${USER_API_BASE}/next-event`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse(response);
  },
};

// ============ NEWS SERVICE ============
export const newsService = {
  getAll: async (): Promise<News[]> => {
    const response = await fetch(`${STUDENT_API_BASE}/news`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse<News[]>(response);
  },

  getById: async (id: number): Promise<News | undefined> => {
    const response = await fetch(`${STUDENT_API_BASE}/news/${id}`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse<News>(response);
  },
};

// ============ CLUBS SERVICE ============
export const clubsService = {
  getAll: async (): Promise<Club[]> => {
    const response = await fetch(`${STUDENT_API_BASE}/clubs`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse<Club[]>(response);
  },

  getById: async (id: number): Promise<Club | undefined> => {
    const response = await fetch(`${STUDENT_API_BASE}/clubs/${id}`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse<Club>(response);
  },
};

// ============ PROJECTS SERVICE ============
export const projectsService = {
  getAll: async (): Promise<Project[]> => {
    const response = await fetch(`${STUDENT_API_BASE}/projects`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse<Project[]>(response);
  },

  getById: async (id: number): Promise<Project | undefined> => {
    const response = await fetch(`${STUDENT_API_BASE}/projects/${id}`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse<Project>(response);
  },
};

// ============ SCHEDULE SERVICE ============
export const scheduleService = {
  getAll: async (): Promise<Schedule[]> => {
    const response = await fetch(`${STUDENT_API_BASE}/schedule`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse<Schedule[]>(response);
  },
};

// ============ INTERNSHIPS SERVICE ============
export const internshipsService = {
  getAll: async (): Promise<Internship[]> => {
    const response = await fetch(`${STUDENT_API_BASE}/internships`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse<Internship[]>(response);
  },
};

// ============ CERTIFICATES SERVICE ============
export const certificateService = {
  getAll: async (): Promise<Certificate[]> => {
    const response = await fetch(`${STUDENT_API_BASE}/certificates`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse<Certificate[]>(response);
  },

  create: async (cert: Omit<Certificate, 'id'>): Promise<Certificate> => {
    const response = await fetch(`${STUDENT_API_BASE}/certificates`, {
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


}
  maxParticipants?: number;
  location?: string;
  date: string;
  description: string;
  title: string;
export interface CreateEventRequest {

}
  contactInfo?: string;
  lookingFor?: string;
  category: string;
  description: string;
  title: string;
export interface CreateProjectRequest {
// ============ TYPES ============

};
  },
    return handleResponse<EventRegistration[]>(response);
    });
      headers: getHeaders(),
      method: 'GET',
    const response = await fetch(`${API_BASE_URL}/api/admin/events/${eventId}/registrations`, {
  getRegistrations: async (eventId: number): Promise<EventRegistration[]> => {

  },
    return handleResponse<Event[]>(response);
    });
      headers: getHeaders(),
      method: 'GET',
    const response = await fetch(`${API_BASE_URL}/api/admin/events`, {
  getAll: async (): Promise<Event[]> => {

  },
    return handleResponse<Event>(response);
    });
      body: JSON.stringify(event),
      headers: getHeaders(),
      method: 'POST',
    const response = await fetch(`${API_BASE_URL}/api/admin/events`, {
  create: async (event: CreateEventRequest): Promise<Event> => {
export const adminEventService = {
// ============ ADMIN EVENTS SERVICE ============

};
  },
    return handleResponse<ProjectApplication[]>(response);
    });
      headers: getHeaders(),
      method: 'GET',
    const response = await fetch(`${STUDENT_API_BASE}/projects/applications`, {
  getApplicationsForMyProjects: async (): Promise<ProjectApplication[]> => {

  },
    return handleResponse<ProjectApplication[]>(response);
    });
      headers: getHeaders(),
      method: 'GET',
    const response = await fetch(`${STUDENT_API_BASE}/projects/my-applications`, {
  getMyApplications: async (): Promise<ProjectApplication[]> => {

  },
    return handleResponse<ProjectApplication>(response);
    });
      body: JSON.stringify({ projectId, experience }),
      headers: getHeaders(),
      method: 'POST',
    const response = await fetch(`${STUDENT_API_BASE}/projects/${projectId}/apply`, {
  applyToProject: async (projectId: number, experience: string): Promise<ProjectApplication> => {

  },
    return handleResponse<Project>(response);
    });
      body: JSON.stringify(project),
      headers: getHeaders(),
      method: 'POST',
    const response = await fetch(`${STUDENT_API_BASE}/projects`, {
  create: async (project: CreateProjectRequest): Promise<Project> => {
export const projectsEnhancedService = {
// ============ PROJECTS ENHANCED SERVICE ============

};
  },
    return handleResponse<EventRegistration>(response);
    });
      body: JSON.stringify({ eventId }),
      headers: getHeaders(),
      method: 'POST',
    const response = await fetch(`${STUDENT_API_BASE}/events/${eventId}/register`, {
  register: async (eventId: number): Promise<EventRegistration> => {

  },
    return handleResponse<Event>(response);
    });
      headers: getHeaders(),
      method: 'GET',
    const response = await fetch(`${STUDENT_API_BASE}/events/${id}`, {
  getById: async (id: number): Promise<Event | undefined> => {

  },
    return handleResponse<Event[]>(response);
    });
      headers: getHeaders(),
      method: 'GET',
    const response = await fetch(`${STUDENT_API_BASE}/events`, {
  getAll: async (): Promise<Event[]> => {
export const eventService = {
// ============ EVENTS SERVICE ============
