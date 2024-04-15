CREATE TABLE IF NOT EXISTS squads
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL UNIQUE,
    commander_mbn VARCHAR(255) REFERENCES militaries (mbn) ON DELETE SET NULL,
    created_at    TIMESTAMPTZ  NOT NULL,
    updated_at    TIMESTAMPTZ  NOT NULL,
    platoon_id    INTEGER REFERENCES platoons (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS squads_commander_mbn_index ON squads (commander_mbn);

COMMENT ON TABLE squads IS 'Отделения';

COMMENT ON COLUMN squads.id IS 'ID';
COMMENT ON COLUMN squads.name IS 'Наименование';
COMMENT ON COLUMN squads.commander_mbn IS 'Уникальный номер жетона командира';
COMMENT ON COLUMN squads.created_at IS 'Время создания';
COMMENT ON COLUMN squads.updated_at IS 'Время последнего изменения';
COMMENT ON COLUMN squads.platoon_id IS 'ID взвода';