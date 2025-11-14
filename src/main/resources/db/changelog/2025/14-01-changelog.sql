-- liquibase formatted sql

-- changeset kurumi:1763111858903-1
CREATE TABLE admin_profiles (user_id UUID NOT NULL, university_id UUID, CONSTRAINT pk_admin_profiles PRIMARY KEY (user_id));

-- changeset kurumi:1763111858903-2
CREATE TABLE certificate_requests (id UUID NOT NULL, user_id UUID NOT NULL, student_profile_id UUID NOT NULL, university_id UUID NOT NULL, status VARCHAR(255) NOT NULL, type VARCHAR(255) NOT NULL, comment VARCHAR(255), created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL, processed_at TIMESTAMP WITHOUT TIME ZONE, processed_by_admin_id UUID, file_object_key VARCHAR(255), CONSTRAINT pk_certificate_requests PRIMARY KEY (id));

-- changeset kurumi:1763111858903-3
CREATE TABLE clubs (id UUID NOT NULL, name VARCHAR(255) NOT NULL, description TEXT NOT NULL, image_object_key VARCHAR(255), creator_id UUID NOT NULL, university_id UUID NOT NULL, CONSTRAINT pk_clubs PRIMARY KEY (id));

-- changeset kurumi:1763111858903-4
CREATE TABLE internships (id UUID NOT NULL, university_id UUID NOT NULL, title VARCHAR(255) NOT NULL, description TEXT NOT NULL, company_name VARCHAR(255) NOT NULL, location VARCHAR(255), start_date date, end_date date, is_paid BOOLEAN, creator_id UUID NOT NULL, CONSTRAINT pk_internships PRIMARY KEY (id));

-- changeset kurumi:1763111858903-5
CREATE TABLE lessons (id UUID NOT NULL, schedule_id UUID NOT NULL, day_of_week VARCHAR(255) NOT NULL, start_time time WITHOUT TIME ZONE NOT NULL, end_time time WITHOUT TIME ZONE NOT NULL, subject VARCHAR(255) NOT NULL, teacher_name VARCHAR(255) NOT NULL, location VARCHAR(255) NOT NULL, CONSTRAINT pk_lessons PRIMARY KEY (id));

-- changeset kurumi:1763111858903-6
CREATE TABLE news (id UUID NOT NULL, university_id UUID NOT NULL, title VARCHAR(255) NOT NULL, content TEXT NOT NULL, image_object_key VARCHAR(255), created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL, published_at TIMESTAMP WITHOUT TIME ZONE, is_published BOOLEAN NOT NULL, CONSTRAINT pk_news PRIMARY KEY (id));

-- changeset kurumi:1763111858903-7
CREATE TABLE projects (id UUID NOT NULL, creator_id UUID NOT NULL, image_object_key VARCHAR(255), description TEXT NOT NULL, title VARCHAR(255) NOT NULL, university_id UUID NOT NULL, CONSTRAINT pk_projects PRIMARY KEY (id));

-- changeset kurumi:1763111858903-8
CREATE TABLE schedules (id UUID NOT NULL, name VARCHAR(255) NOT NULL, university_id UUID, CONSTRAINT pk_schedules PRIMARY KEY (id));

-- changeset kurumi:1763111858903-9
CREATE TABLE student_profiles (user_id UUID NOT NULL, first_name VARCHAR(255) NOT NULL, last_name VARCHAR(255) NOT NULL, student_number VARCHAR(255) NOT NULL, group_name VARCHAR(255) NOT NULL, direction VARCHAR(255) NOT NULL, university_id UUID, schedule_id UUID, CONSTRAINT pk_student_profiles PRIMARY KEY (user_id));

-- changeset kurumi:1763111858903-10
CREATE TABLE universities (id UUID NOT NULL, name VARCHAR(255) NOT NULL, address VARCHAR(255) NOT NULL, CONSTRAINT pk_universities PRIMARY KEY (id));

-- changeset kurumi:1763111858903-11
CREATE TABLE users (id UUID NOT NULL, service_id UUID NOT NULL, user_type VARCHAR(255) NOT NULL, avatar_url VARCHAR(255), CONSTRAINT pk_users PRIMARY KEY (id));

-- changeset kurumi:1763111858903-12
ALTER TABLE student_profiles ADD CONSTRAINT uc_student_profiles_student_number UNIQUE (student_number);

-- changeset kurumi:1763111858903-13
ALTER TABLE users ADD CONSTRAINT uc_users_service UNIQUE (service_id);

-- changeset kurumi:1763111858903-14
ALTER TABLE admin_profiles ADD CONSTRAINT FK_ADMIN_PROFILES_ON_UNIVERSITY FOREIGN KEY (university_id) REFERENCES universities (id);

-- changeset kurumi:1763111858903-15
ALTER TABLE admin_profiles ADD CONSTRAINT FK_ADMIN_PROFILES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

-- changeset kurumi:1763111858903-16
ALTER TABLE certificate_requests ADD CONSTRAINT FK_CERTIFICATE_REQUESTS_ON_PROCESSED_BY_ADMIN FOREIGN KEY (processed_by_admin_id) REFERENCES admin_profiles (user_id);

-- changeset kurumi:1763111858903-17
ALTER TABLE certificate_requests ADD CONSTRAINT FK_CERTIFICATE_REQUESTS_ON_STUDENT_PROFILE FOREIGN KEY (student_profile_id) REFERENCES student_profiles (user_id);

-- changeset kurumi:1763111858903-18
ALTER TABLE certificate_requests ADD CONSTRAINT FK_CERTIFICATE_REQUESTS_ON_UNIVERSITY FOREIGN KEY (university_id) REFERENCES universities (id);

-- changeset kurumi:1763111858903-19
ALTER TABLE certificate_requests ADD CONSTRAINT FK_CERTIFICATE_REQUESTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

-- changeset kurumi:1763111858903-20
ALTER TABLE clubs ADD CONSTRAINT FK_CLUBS_ON_CREATOR FOREIGN KEY (creator_id) REFERENCES users (id);

-- changeset kurumi:1763111858903-21
ALTER TABLE clubs ADD CONSTRAINT FK_CLUBS_ON_UNIVERSITY FOREIGN KEY (university_id) REFERENCES universities (id);

-- changeset kurumi:1763111858903-22
ALTER TABLE internships ADD CONSTRAINT FK_INTERNSHIPS_ON_CREATOR FOREIGN KEY (creator_id) REFERENCES users (id);

-- changeset kurumi:1763111858903-23
ALTER TABLE internships ADD CONSTRAINT FK_INTERNSHIPS_ON_UNIVERSITY FOREIGN KEY (university_id) REFERENCES universities (id);

-- changeset kurumi:1763111858903-24
ALTER TABLE lessons ADD CONSTRAINT FK_LESSONS_ON_SCHEDULE FOREIGN KEY (schedule_id) REFERENCES schedules (id);

-- changeset kurumi:1763111858903-25
ALTER TABLE news ADD CONSTRAINT FK_NEWS_ON_UNIVERSITY FOREIGN KEY (university_id) REFERENCES universities (id);

-- changeset kurumi:1763111858903-26
ALTER TABLE projects ADD CONSTRAINT FK_PROJECTS_ON_CREATOR FOREIGN KEY (creator_id) REFERENCES users (id);

-- changeset kurumi:1763111858903-27
ALTER TABLE projects ADD CONSTRAINT FK_PROJECTS_ON_UNIVERSITY FOREIGN KEY (university_id) REFERENCES universities (id);

-- changeset kurumi:1763111858903-28
ALTER TABLE schedules ADD CONSTRAINT FK_SCHEDULES_ON_UNIVERSITY FOREIGN KEY (university_id) REFERENCES universities (id);

-- changeset kurumi:1763111858903-29
ALTER TABLE student_profiles ADD CONSTRAINT FK_STUDENT_PROFILES_ON_SCHEDULE FOREIGN KEY (schedule_id) REFERENCES schedules (id);

-- changeset kurumi:1763111858903-30
ALTER TABLE student_profiles ADD CONSTRAINT FK_STUDENT_PROFILES_ON_UNIVERSITY FOREIGN KEY (university_id) REFERENCES universities (id);

-- changeset kurumi:1763111858903-31
ALTER TABLE student_profiles ADD CONSTRAINT FK_STUDENT_PROFILES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);
