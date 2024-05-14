DO
$$
    BEGIN
        CREATE TYPE COORDINATE AS
        (
            lat DOUBLE PRECISION,
            lng DOUBLE PRECISION
        );

        COMMENT ON TYPE COORDINATE IS 'Коодината';
        COMMENT ON COLUMN coordinate.lat IS 'Широта';
        COMMENT ON COLUMN coordinate.lng IS 'Долгота';
    EXCEPTION
        WHEN duplicate_object THEN NULL;
    END
$$;

DO
$$
    BEGIN
        CREATE TYPE ADDRESS AS
        (
            post_code    INTEGER,
            country      VARCHAR(255),
            state        VARCHAR(255),
            locality     VARCHAR(255),
            street       VARCHAR(255),
            house_number VARCHAR(255)
        );

        COMMENT ON TYPE ADDRESS IS 'Адрес';
        COMMENT ON COLUMN address.post_code IS 'Почтовый индекс';
        COMMENT ON COLUMN address.country IS 'Страна';
        COMMENT ON COLUMN address.state IS 'Субъект';
        COMMENT ON COLUMN address.locality IS 'Город';
        COMMENT ON COLUMN address.street IS 'Улица';
        COMMENT ON COLUMN address.house_number IS 'Дом';
    EXCEPTION
        WHEN duplicate_object THEN NULL;
    END
$$;

CREATE TABLE IF NOT EXISTS units
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL UNIQUE,
    coordinate    COORDINATE,
    address       ADDRESS,
    commander_mbn VARCHAR(255) REFERENCES militaries (mbn) ON DELETE SET NULL ON UPDATE CASCADE,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS units_commander_mbn_index ON units (commander_mbn);

COMMENT ON TABLE units IS 'Воинские части';

COMMENT ON COLUMN units.id IS 'ID';
COMMENT ON COLUMN units.name IS 'Наименование';
COMMENT ON COLUMN units.coordinate IS 'Координаты';
COMMENT ON COLUMN units.address IS 'Адрес';
COMMENT ON COLUMN units.created_at IS 'Время создания';
COMMENT ON COLUMN units.updated_at IS 'Время последнего изменения';
COMMENT ON COLUMN units.commander_mbn IS 'Уникальный номер жетона командира';