CREATE TABLE IF NOT EXISTS divisions
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL UNIQUE,
    commander_mbn VARCHAR(255) REFERENCES militaries (mbn) ON DELETE SET NULL ON UPDATE CASCADE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS divisions_commander_mbn_index ON divisions (commander_mbn);

COMMENT ON TABLE divisions IS 'Дивизии';

COMMENT ON COLUMN divisions.id IS 'ID';
COMMENT ON COLUMN divisions.name IS 'Наименование';
COMMENT ON COLUMN divisions.commander_mbn IS 'Уникальный номер жетона командира';
COMMENT ON COLUMN divisions.created_at IS 'Время создания';
COMMENT ON COLUMN divisions.updated_at IS 'Время последнего изменения';