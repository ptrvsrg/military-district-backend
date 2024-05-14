CREATE TABLE IF NOT EXISTS corps
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL UNIQUE,
    commander_mbn VARCHAR(255) REFERENCES militaries (mbn) ON DELETE SET NULL ON UPDATE CASCADE,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS corps_commander_mbn_index ON corps (commander_mbn);

COMMENT ON TABLE corps IS 'Корпус';

COMMENT ON COLUMN corps.id IS 'ID';
COMMENT ON COLUMN corps.name IS 'Наименование';
COMMENT ON COLUMN corps.commander_mbn IS 'Уникальный номер жетона командира';
COMMENT ON COLUMN corps.created_at IS 'Время создания';
COMMENT ON COLUMN corps.updated_at IS 'Время последнего изменения';