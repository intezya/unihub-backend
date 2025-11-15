-- liquibase formatted sql

-- changeset kurumi:20251115-1
ALTER TABLE users ADD COLUMN active BOOLEAN NOT NULL DEFAULT TRUE;
