CREATE TABLE IF NOT EXISTS combat_equipments
(
    id            SERIAL PRIMARY KEY,
    serial_number VARCHAR(255) NOT NULL UNIQUE,
    type_id       INTEGER REFERENCES combat_equipment_types (id) ON DELETE CASCADE,
    unit_name     VARCHAR(255) REFERENCES units (name) ON DELETE SET NULL,
    created_at    TIMESTAMPTZ  NOT NULL,
    updated_at    TIMESTAMPTZ  NOT NULL
);

COMMENT ON TABLE combat_equipments IS 'Боевая техника';

COMMENT ON COLUMN combat_equipments.id IS 'ID';
COMMENT ON COLUMN combat_equipments.serial_number IS 'Серийный номер';
COMMENT ON COLUMN combat_equipments.type_id IS 'ID вида боевой техники';
COMMENT ON COLUMN combat_equipments.unit_name IS 'Наименование воинской части';
COMMENT ON COLUMN combat_equipments.created_at IS 'Время создания';
COMMENT ON COLUMN combat_equipments.updated_at IS 'Время последнего изменения';