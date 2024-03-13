CREATE TABLE IF NOT EXISTS militaries
(
    id          SERIAL PRIMARY KEY,
    mbn         VARCHAR(255) NOT NULL UNIQUE,
    first_name  VARCHAR(255) NOT NULL,
    last_name   VARCHAR(255) NOT NULL,
    middle_name VARCHAR(255),
    birth_date  DATE         NOT NULL CHECK (birth_date <= (CURRENT_DATE - '18 years'::INTERVAL)),
    avatar_url  TEXT,
    rank_id     INTEGER      REFERENCES ranks (id) ON DELETE SET NULL,
    unit_number INTEGER,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

COMMENT ON TABLE militaries IS 'Военнослужащие';

COMMENT ON COLUMN militaries.mbn IS 'Уникальный номер жетона военнослужащего';
COMMENT ON COLUMN militaries.first_name IS 'Имя';
COMMENT ON COLUMN militaries.last_name IS 'Фамилия';
COMMENT ON COLUMN militaries.middle_name IS 'Отчество';
COMMENT ON COLUMN militaries.birth_date IS 'Дата рождения';
COMMENT ON COLUMN militaries.avatar_url IS 'URL фотографии профиля';
COMMENT ON COLUMN militaries.rank_id IS 'ID воинского звания';
COMMENT ON COLUMN militaries.unit_number IS 'Номер воинской части, к которой прикреплён военнослужащий';
COMMENT ON COLUMN militaries.created_at IS 'Время создания';
COMMENT ON COLUMN militaries.updated_at IS 'Время последнего изменения';