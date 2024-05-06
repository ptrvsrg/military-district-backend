CREATE TABLE IF NOT EXISTS platoons
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL UNIQUE,
    commander_mbn VARCHAR(255) REFERENCES militaries (mbn) ON DELETE SET NULL ON UPDATE CASCADE,
    created_at    TIMESTAMPTZ  NOT NULL,
    updated_at    TIMESTAMPTZ  NOT NULL,
    company_id    INTEGER REFERENCES companies (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX IF NOT EXISTS platoons_commander_mbn_index ON platoons (commander_mbn);

COMMENT ON TABLE platoons IS 'Взводы';

COMMENT ON COLUMN platoons.id IS 'ID';
COMMENT ON COLUMN platoons.name IS 'Наименование';
COMMENT ON COLUMN platoons.commander_mbn IS 'Уникальный номер жетона командира';
COMMENT ON COLUMN platoons.created_at IS 'Время создания';
COMMENT ON COLUMN platoons.updated_at IS 'Время последнего изменения';
COMMENT ON COLUMN platoons.company_id IS 'ID роты';