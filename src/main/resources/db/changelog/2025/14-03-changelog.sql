-- liquibase formatted sql

-- changeset kurumi:add-max-user-id-to-users
ALTER TABLE users
ADD COLUMN max_user_id BIGINT NULL,
ADD CONSTRAINT unique_max_user_id UNIQUE (max_user_id);

COMMENT ON COLUMN users.max_user_id IS 'MAX Bridge user ID for authentication';
