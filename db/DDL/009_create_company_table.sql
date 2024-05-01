CREATE TABLE IF NOT EXISTS companies
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL UNIQUE,
    commander_mbn VARCHAR(255) REFERENCES militaries (mbn) ON DELETE SET NULL,
    created_at    TIMESTAMPTZ  NOT NULL,
    updated_at    TIMESTAMPTZ  NOT NULL,
    unit_id       INTEGER      NOT NULL REFERENCES units (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS companies_commander_mbn_index ON companies (commander_mbn);

COMMENT ON TABLE companies IS 'Роты';

COMMENT ON COLUMN companies.id IS 'ID';
COMMENT ON COLUMN companies.name IS 'Наименование';
COMMENT ON COLUMN companies.commander_mbn IS 'Уникальный номер жетона командира';
COMMENT ON COLUMN companies.created_at IS 'Время создания';
COMMENT ON COLUMN companies.updated_at IS 'Время последнего изменения';
COMMENT ON COLUMN companies.unit_id IS 'ID воинской части';