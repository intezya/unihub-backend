-- liquibase formatted sql

-- changeset kurumi:add-project-status-and-category
ALTER TABLE projects ADD COLUMN IF NOT EXISTS status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE';
ALTER TABLE projects ADD COLUMN IF NOT EXISTS category VARCHAR(255) NOT NULL DEFAULT 'IT';

-- changeset kurumi:add-club-category
ALTER TABLE clubs ADD COLUMN IF NOT EXISTS category VARCHAR(255) NOT NULL DEFAULT 'GENERAL';

-- changeset kurumi:add-lesson-type
ALTER TABLE lessons ADD COLUMN IF NOT EXISTS lesson_type VARCHAR(50) NOT NULL DEFAULT 'LECTURE';

-- changeset kurumi:create-club-members-table
CREATE TABLE IF NOT EXISTS club_members (
    club_id UUID NOT NULL,
    student_id UUID NOT NULL,
    PRIMARY KEY (club_id, student_id),
    CONSTRAINT fk_club_members_club FOREIGN KEY (club_id) REFERENCES clubs(id) ON DELETE CASCADE,
    CONSTRAINT fk_club_members_student FOREIGN KEY (student_id) REFERENCES student_profiles(id) ON DELETE CASCADE
);

-- changeset kurumi:create-club-members-indexes
CREATE INDEX IF NOT EXISTS idx_club_members_club_id ON club_members(club_id);
CREATE INDEX IF NOT EXISTS idx_club_members_student_id ON club_members(student_id);
