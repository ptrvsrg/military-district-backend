CREATE TABLE IF NOT EXISTS military_service.specialties
(
    id         SERIAL PRIMARY KEY,
    code       VARCHAR(7)   NOT NULL UNIQUE,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ  NOT NULL,
    updated_at TIMESTAMPTZ  NOT NULL
);

CREATE INDEX IF NOT EXISTS specialties_name_index ON military_service.specialties (name);

COMMENT ON TABLE military_service.specialties IS 'Военно-учётные специальности';

COMMENT ON COLUMN military_service.specialties.id IS 'ID';
COMMENT ON COLUMN military_service.specialties.code IS 'Код';
COMMENT ON COLUMN military_service.specialties.name IS 'Название';
COMMENT ON COLUMN military_service.specialties.created_at IS 'Время создания';
COMMENT ON COLUMN military_service.specialties.updated_at IS 'Время последнего изменения';