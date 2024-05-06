CREATE TABLE IF NOT EXISTS brigades
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL UNIQUE,
    commander_mbn VARCHAR(255) REFERENCES militaries (mbn) ON DELETE SET NULL ON UPDATE CASCADE,
    created_at    TIMESTAMPTZ  NOT NULL,
    updated_at    TIMESTAMPTZ  NOT NULL
);

CREATE INDEX IF NOT EXISTS brigades_commander_mbn_index ON brigades (commander_mbn);

COMMENT ON TABLE brigades IS 'Бригады';

COMMENT ON COLUMN brigades.id IS 'ID';
COMMENT ON COLUMN brigades.name IS 'Наименование';
COMMENT ON COLUMN brigades.commander_mbn IS 'Уникальный номер жетона командира';
COMMENT ON COLUMN brigades.created_at IS 'Время создания';
COMMENT ON COLUMN brigades.updated_at IS 'Время последнего изменения';
