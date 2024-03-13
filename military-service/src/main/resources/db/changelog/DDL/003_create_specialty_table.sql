CREATE TABLE IF NOT EXISTS specialties
(
    id   SERIAL PRIMARY KEY,
    code VARCHAR(7)   NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX IF NOT EXISTS specialties_name_index ON specialties (name);

COMMENT ON TABLE specialties IS 'Военно-учётные специальности';

COMMENT ON COLUMN specialties.id IS 'ID';
COMMENT ON COLUMN specialties.code IS 'Код';
COMMENT ON COLUMN specialties.name IS 'Название';
COMMENT ON COLUMN specialties.created_at IS 'Время создания';
COMMENT ON COLUMN specialties.updated_at IS 'Время последнего изменения';