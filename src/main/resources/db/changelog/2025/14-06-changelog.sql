-- liquibase formatted sql

-- changeset kurumi:add-internship-fields
ALTER TABLE internships
ADD COLUMN logo_url VARCHAR(500),
ADD COLUMN status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
ADD COLUMN external_url VARCHAR(500),
ADD COLUMN direction VARCHAR(100);

COMMENT ON COLUMN internships.logo_url IS 'URL логотипа компании';
COMMENT ON COLUMN internships.status IS 'Статус стажировки (ACTIVE, CLOSED, COMPLETED)';
COMMENT ON COLUMN internships.external_url IS 'Ссылка на сайт компании со стажировкой';
COMMENT ON COLUMN internships.direction IS 'Направление (IT, Маркетинг и т.д.)';

CREATE INDEX idx_internships_status ON internships(status);
