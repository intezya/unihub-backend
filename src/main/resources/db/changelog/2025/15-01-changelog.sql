-- liquibase formatted sql

-- changeset kurumi:1763159051804-1
CREATE TABLE admin_profiles (user_id UUID NOT NULL, university_id UUID, CONSTRAINT pk_admin_profiles PRIMARY KEY (user_id));

-- changeset kurumi:1763159051804-2
CREATE TABLE certificate_requests (id UUID NOT NULL, user_id UUID NOT NULL, student_profile_id UUID NOT NULL, university_id UUID NOT NULL, status VARCHAR(255) NOT NULL, type VARCHAR(255) NOT NULL, comment VARCHAR(255), created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL, processed_at TIMESTAMP WITHOUT TIME ZONE, processed_by_admin_id UUID, file_object_key VARCHAR(255), CONSTRAINT pk_certificate_requests PRIMARY KEY (id));

-- changeset kurumi:1763159051804-3
CREATE TABLE club (id UUID NOT NULL, name VARCHAR(255) NOT NULL, description TEXT NOT NULL, image_object_key VARCHAR(255), creator_id UUID NOT NULL, university_id UUID NOT NULL, category VARCHAR(255) NOT NULL, CONSTRAINT pk_club PRIMARY KEY (id));

-- changeset kurumi:1763159051804-4
CREATE TABLE club_members (club_id UUID NOT NULL, student_id UUID NOT NULL, CONSTRAINT pk_club_members PRIMARY KEY (club_id, student_id));

-- changeset kurumi:1763159051804-5
CREATE TABLE event_registrations (id UUID NOT NULL, registered_at TIMESTAMP WITHOUT TIME ZONE NOT NULL, student_number VARCHAR(255) NOT NULL, student_group VARCHAR(255) NOT NULL, student_name VARCHAR(255) NOT NULL, student_id UUID NOT NULL, event_id UUID NOT NULL, CONSTRAINT pk_event_registrations PRIMARY KEY (id));

-- changeset kurumi:1763159051804-6
CREATE TABLE events (id UUID NOT NULL, title VARCHAR(255) NOT NULL, description TEXT NOT NULL, image_url VARCHAR(255), event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, location VARCHAR(255), max_participants INTEGER, university_id UUID, created_by UUID, created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL, CONSTRAINT pk_events PRIMARY KEY (id));

-- changeset kurumi:1763159051804-7
CREATE TABLE internships (id UUID NOT NULL, university_id UUID, title VARCHAR(255) NOT NULL, description TEXT NOT NULL, company_name VARCHAR(255) NOT NULL, location VARCHAR(255), start_date date, end_date date, is_paid BOOLEAN, logo_url VARCHAR(255), status VARCHAR(255) NOT NULL, external_url VARCHAR(255), direction VARCHAR(255), creator_id UUID, CONSTRAINT pk_internships PRIMARY KEY (id));

-- changeset kurumi:1763159051804-8
CREATE TABLE lessons (id UUID NOT NULL, schedule_id UUID NOT NULL, day_of_week VARCHAR(255) NOT NULL, start_time time WITHOUT TIME ZONE NOT NULL, end_time time WITHOUT TIME ZONE NOT NULL, subject VARCHAR(255) NOT NULL, teacher_name VARCHAR(255) NOT NULL, location VARCHAR(255) NOT NULL, lesson_type VARCHAR(255) NOT NULL, CONSTRAINT pk_lessons PRIMARY KEY (id));

-- changeset kurumi:1763159051804-9
CREATE TABLE news (id UUID NOT NULL, university_id UUID NOT NULL, title VARCHAR(255) NOT NULL, content TEXT NOT NULL, image_object_key VARCHAR(255), created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL, published_at TIMESTAMP WITHOUT TIME ZONE, is_published BOOLEAN NOT NULL, CONSTRAINT pk_news PRIMARY KEY (id));

-- changeset kurumi:1763159051804-10
CREATE TABLE project_applications (id UUID NOT NULL, project_id UUID NOT NULL, applicant_id UUID NOT NULL, applicant_name VARCHAR(255) NOT NULL, applicant_email VARCHAR(255) NOT NULL, experience TEXT NOT NULL, applied_at TIMESTAMP WITHOUT TIME ZONE NOT NULL, status VARCHAR(255) NOT NULL, CONSTRAINT pk_project_applications PRIMARY KEY (id));

-- changeset kurumi:1763159051804-11
CREATE TABLE projects (id UUID NOT NULL, creator_id UUID NOT NULL, image_object_key VARCHAR(255), description TEXT NOT NULL, title VARCHAR(255) NOT NULL, university_id UUID NOT NULL, status VARCHAR(255) NOT NULL, category VARCHAR(255) NOT NULL, looking_for TEXT, contact_info VARCHAR(255), created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL, CONSTRAINT pk_projects PRIMARY KEY (id));

-- changeset kurumi:1763159051804-12
CREATE TABLE schedules (id UUID NOT NULL, name VARCHAR(255) NOT NULL, university_id UUID, CONSTRAINT pk_schedules PRIMARY KEY (id));

-- changeset kurumi:1763159051804-13
CREATE TABLE student_profiles (user_id UUID NOT NULL, first_name VARCHAR(255) NOT NULL, last_name VARCHAR(255) NOT NULL, student_number VARCHAR(255) NOT NULL, group_name VARCHAR(255) NOT NULL, direction VARCHAR(255) NOT NULL, university_id UUID, schedule_id UUID, CONSTRAINT pk_student_profiles PRIMARY KEY (user_id));

-- changeset kurumi:1763159051804-14
CREATE TABLE universities (id UUID NOT NULL, name VARCHAR(255) NOT NULL, address VARCHAR(255) NOT NULL, CONSTRAINT pk_universities PRIMARY KEY (id));

-- changeset kurumi:1763159051804-15
CREATE TABLE users (id UUID NOT NULL, service_id UUID NOT NULL, user_type VARCHAR(255) NOT NULL, avatar_url VARCHAR(255), max_user_id BIGINT, CONSTRAINT pk_users PRIMARY KEY (id));

-- changeset kurumi:1763159051804-16
ALTER TABLE student_profiles ADD CONSTRAINT uc_student_profiles_student_number UNIQUE (student_number);

-- changeset kurumi:1763159051804-17
ALTER TABLE users ADD CONSTRAINT uc_users_max_user UNIQUE (max_user_id);

-- changeset kurumi:1763159051804-18
ALTER TABLE users ADD CONSTRAINT uc_users_service UNIQUE (service_id);

-- changeset kurumi:1763159051804-19
ALTER TABLE admin_profiles ADD CONSTRAINT FK_ADMIN_PROFILES_ON_UNIVERSITY FOREIGN KEY (university_id) REFERENCES universities (id);

-- changeset kurumi:1763159051804-20
ALTER TABLE admin_profiles ADD CONSTRAINT FK_ADMIN_PROFILES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

-- changeset kurumi:1763159051804-21
ALTER TABLE certificate_requests ADD CONSTRAINT FK_CERTIFICATE_REQUESTS_ON_PROCESSED_BY_ADMIN FOREIGN KEY (processed_by_admin_id) REFERENCES admin_profiles (user_id);

-- changeset kurumi:1763159051804-22
ALTER TABLE certificate_requests ADD CONSTRAINT FK_CERTIFICATE_REQUESTS_ON_STUDENT_PROFILE FOREIGN KEY (student_profile_id) REFERENCES student_profiles (user_id);

-- changeset kurumi:1763159051804-23
ALTER TABLE certificate_requests ADD CONSTRAINT FK_CERTIFICATE_REQUESTS_ON_UNIVERSITY FOREIGN KEY (university_id) REFERENCES universities (id);

-- changeset kurumi:1763159051804-24
ALTER TABLE certificate_requests ADD CONSTRAINT FK_CERTIFICATE_REQUESTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

-- changeset kurumi:1763159051804-25
ALTER TABLE club ADD CONSTRAINT FK_CLUB_ON_CREATOR FOREIGN KEY (creator_id) REFERENCES users (id);

-- changeset kurumi:1763159051804-26
ALTER TABLE club ADD CONSTRAINT FK_CLUB_ON_UNIVERSITY FOREIGN KEY (university_id) REFERENCES universities (id);

-- changeset kurumi:1763159051804-27
ALTER TABLE events ADD CONSTRAINT FK_EVENTS_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES users (id);

-- changeset kurumi:1763159051804-28
ALTER TABLE events ADD CONSTRAINT FK_EVENTS_ON_UNIVERSITY FOREIGN KEY (university_id) REFERENCES universities (id);

-- changeset kurumi:1763159051804-29
ALTER TABLE event_registrations ADD CONSTRAINT FK_EVENT_REGISTRATIONS_ON_EVENT FOREIGN KEY (event_id) REFERENCES events (id);

-- changeset kurumi:1763159051804-30
ALTER TABLE event_registrations ADD CONSTRAINT FK_EVENT_REGISTRATIONS_ON_STUDENT FOREIGN KEY (student_id) REFERENCES student_profiles (user_id);

-- changeset kurumi:1763159051804-31
ALTER TABLE internships ADD CONSTRAINT FK_INTERNSHIPS_ON_CREATOR FOREIGN KEY (creator_id) REFERENCES users (id);

-- changeset kurumi:1763159051804-32
ALTER TABLE internships ADD CONSTRAINT FK_INTERNSHIPS_ON_UNIVERSITY FOREIGN KEY (university_id) REFERENCES universities (id);

-- changeset kurumi:1763159051804-33
ALTER TABLE lessons ADD CONSTRAINT FK_LESSONS_ON_SCHEDULE FOREIGN KEY (schedule_id) REFERENCES schedules (id);

-- changeset kurumi:1763159051804-34
ALTER TABLE news ADD CONSTRAINT FK_NEWS_ON_UNIVERSITY FOREIGN KEY (university_id) REFERENCES universities (id);

-- changeset kurumi:1763159051804-35
ALTER TABLE projects ADD CONSTRAINT FK_PROJECTS_ON_CREATOR FOREIGN KEY (creator_id) REFERENCES users (id);

-- changeset kurumi:1763159051804-36
ALTER TABLE projects ADD CONSTRAINT FK_PROJECTS_ON_UNIVERSITY FOREIGN KEY (university_id) REFERENCES universities (id);

-- changeset kurumi:1763159051804-37
ALTER TABLE project_applications ADD CONSTRAINT FK_PROJECT_APPLICATIONS_ON_APPLICANT FOREIGN KEY (applicant_id) REFERENCES student_profiles (user_id);

-- changeset kurumi:1763159051804-38
ALTER TABLE project_applications ADD CONSTRAINT FK_PROJECT_APPLICATIONS_ON_PROJECT FOREIGN KEY (project_id) REFERENCES projects (id);

-- changeset kurumi:1763159051804-39
ALTER TABLE schedules ADD CONSTRAINT FK_SCHEDULES_ON_UNIVERSITY FOREIGN KEY (university_id) REFERENCES universities (id);

-- changeset kurumi:1763159051804-40
ALTER TABLE student_profiles ADD CONSTRAINT FK_STUDENT_PROFILES_ON_SCHEDULE FOREIGN KEY (schedule_id) REFERENCES schedules (id);

-- changeset kurumi:1763159051804-41
ALTER TABLE student_profiles ADD CONSTRAINT FK_STUDENT_PROFILES_ON_UNIVERSITY FOREIGN KEY (university_id) REFERENCES universities (id);

-- changeset kurumi:1763159051804-42
ALTER TABLE student_profiles ADD CONSTRAINT FK_STUDENT_PROFILES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

-- changeset kurumi:1763159051804-43
ALTER TABLE club_members ADD CONSTRAINT fk_clumem_on_club FOREIGN KEY (club_id) REFERENCES club (id);

-- changeset kurumi:1763159051804-44
ALTER TABLE club_members ADD CONSTRAINT fk_clumem_on_student_profile FOREIGN KEY (student_id) REFERENCES student_profiles (user_id);
