CREATE TABLE IF NOT EXISTS armies
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL UNIQUE,
    commander_mbn VARCHAR(255) REFERENCES militaries (mbn) ON DELETE SET NULL,
    created_at    TIMESTAMPTZ  NOT NULL,
    updated_at    TIMESTAMPTZ  NOT NULL
);

CREATE INDEX IF NOT EXISTS armies_commander_mbn_index ON armies (commander_mbn);

COMMENT ON TABLE armies IS 'Армии';

COMMENT ON COLUMN armies.id IS 'ID';
COMMENT ON COLUMN armies.name IS 'Наименование';
COMMENT ON COLUMN armies.commander_mbn IS 'Уникальный номер жетона командира';
COMMENT ON COLUMN armies.created_at IS 'Время создания';
COMMENT ON COLUMN armies.updated_at IS 'Время последнего изменения';