CREATE TABLE IF NOT EXISTS weapons
(
    id            SERIAL PRIMARY KEY,
    serial_number VARCHAR(255) NOT NULL UNIQUE,
    type_id       INTEGER REFERENCES weapon_types (id) ON DELETE SET NULL ON UPDATE CASCADE,
    unit_name     VARCHAR(255) REFERENCES units (name) ON DELETE SET NULL ON UPDATE CASCADE,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE weapons IS 'Вооружение';

COMMENT ON COLUMN weapons.id IS 'ID';
COMMENT ON COLUMN weapons.serial_number IS 'Серийный номер';
COMMENT ON COLUMN weapons.type_id IS 'ID вида вооружения';
COMMENT ON COLUMN weapons.unit_name IS 'Наименование воинской части';
COMMENT ON COLUMN weapons.created_at IS 'Время создания';
COMMENT ON COLUMN weapons.updated_at IS 'Время последнего изменения';