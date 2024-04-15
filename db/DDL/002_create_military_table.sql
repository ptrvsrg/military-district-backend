CREATE TABLE IF NOT EXISTS militaries
(
    id          SERIAL PRIMARY KEY,
    mbn         VARCHAR(255) NOT NULL UNIQUE,
    first_name  VARCHAR(255) NOT NULL,
    last_name   VARCHAR(255) NOT NULL,
    middle_name VARCHAR(255),
    birth_date  DATE         NOT NULL,
    rank_id     INTEGER      REFERENCES ranks (id) ON DELETE SET NULL,
    created_at  TIMESTAMPTZ  NOT NULL,
    updated_at  TIMESTAMPTZ  NOT NULL,
    unit_name   VARCHAR(255)
);

CREATE INDEX IF NOT EXISTS militaries_first_name_index ON militaries (first_name);
CREATE INDEX IF NOT EXISTS militaries_last_name_index ON militaries (last_name);
CREATE INDEX IF NOT EXISTS militaries_middle_name_index ON militaries (middle_name);
CREATE INDEX IF NOT EXISTS militaries_unit_name_index ON militaries (unit_name);

COMMENT ON TABLE militaries IS 'Военнослужащие';

COMMENT ON COLUMN militaries.id IS 'ID';
COMMENT ON COLUMN militaries.mbn IS 'Уникальный номер жетона военнослужащего';
COMMENT ON COLUMN militaries.first_name IS 'Имя';
COMMENT ON COLUMN militaries.last_name IS 'Фамилия';
COMMENT ON COLUMN militaries.middle_name IS 'Отчество';
COMMENT ON COLUMN militaries.birth_date IS 'Дата рождения';
COMMENT ON COLUMN militaries.rank_id IS 'ID воинского звания';
COMMENT ON COLUMN militaries.created_at IS 'Время создания';
COMMENT ON COLUMN militaries.updated_at IS 'Время последнего изменения';
COMMENT ON COLUMN militaries.unit_name IS 'Наименование воинской части, к которой прикреплён военнослужащий';