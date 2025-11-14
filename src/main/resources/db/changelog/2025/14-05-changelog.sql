-- liquibase formatted sql

-- changeset kurumi:add-project-fields
ALTER TABLE projects
ADD COLUMN looking_for TEXT,
ADD COLUMN contact_info VARCHAR(255),
ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

COMMENT ON COLUMN projects.looking_for IS 'Кого ищут в команду';
COMMENT ON COLUMN projects.contact_info IS 'Контактная информация';
COMMENT ON COLUMN projects.created_at IS 'Дата создания проекта';

-- changeset kurumi:create-project-applications-table
CREATE TABLE project_applications (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL,
    applicant_id UUID NOT NULL,
    applicant_name VARCHAR(255) NOT NULL,
    applicant_email VARCHAR(255) NOT NULL,
    experience TEXT NOT NULL,
    applied_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL DEFAULT 'NEW',
    CONSTRAINT fk_project_applications_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_project_applications_applicant FOREIGN KEY (applicant_id) REFERENCES student_profiles(id)
);

CREATE INDEX idx_project_applications_project ON project_applications(project_id);
CREATE INDEX idx_project_applications_applicant ON project_applications(applicant_id);
CREATE INDEX idx_project_applications_status ON project_applications(status);

COMMENT ON TABLE project_applications IS 'Отклики на проекты';
COMMENT ON COLUMN project_applications.applicant_name IS 'ФИО откликнувшегося';
COMMENT ON COLUMN project_applications.applicant_email IS 'Email откликнувшегося';
COMMENT ON COLUMN project_applications.experience IS 'Описание опыта';
COMMENT ON COLUMN project_applications.status IS 'Статус отклика (NEW, REVIEWED, ACCEPTED, REJECTED)';
