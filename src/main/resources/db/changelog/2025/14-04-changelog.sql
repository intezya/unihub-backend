-- liquibase formatted sql

-- changeset kurumi:create-events-table
CREATE TABLE events (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    image_url VARCHAR(500),
    event_date TIMESTAMP NOT NULL,
    location VARCHAR(255),
    max_participants INTEGER,
    university_id UUID,
    created_by UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_events_university FOREIGN KEY (university_id) REFERENCES universities(id),
    CONSTRAINT fk_events_created_by FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE INDEX idx_events_university ON events(university_id);
CREATE INDEX idx_events_date ON events(event_date);

COMMENT ON TABLE events IS 'Мероприятия университета';
COMMENT ON COLUMN events.title IS 'Название мероприятия';
COMMENT ON COLUMN events.description IS 'Описание мероприятия';
COMMENT ON COLUMN events.image_url IS 'URL изображения мероприятия';
COMMENT ON COLUMN events.event_date IS 'Дата и время проведения мероприятия';
COMMENT ON COLUMN events.location IS 'Место проведения';
COMMENT ON COLUMN events.max_participants IS 'Максимальное количество участников';

-- changeset kurumi:create-event-registrations-table
CREATE TABLE event_registrations (
    id UUID PRIMARY KEY,
    event_id UUID NOT NULL,
    student_id UUID NOT NULL,
    student_name VARCHAR(255) NOT NULL,
    student_group VARCHAR(100) NOT NULL,
    student_number VARCHAR(50) NOT NULL,
    registered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_event_registrations_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    CONSTRAINT fk_event_registrations_student FOREIGN KEY (student_id) REFERENCES student_profiles(id),
    CONSTRAINT uk_event_student UNIQUE (event_id, student_id)
);

CREATE INDEX idx_event_registrations_event ON event_registrations(event_id);
CREATE INDEX idx_event_registrations_student ON event_registrations(student_id);

COMMENT ON TABLE event_registrations IS 'Регистрации студентов на мероприятия';
COMMENT ON COLUMN event_registrations.student_name IS 'ФИО студента';
COMMENT ON COLUMN event_registrations.student_group IS 'Группа студента';
COMMENT ON COLUMN event_registrations.student_number IS 'Номер студенческого билета';
