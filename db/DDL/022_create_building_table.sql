CREATE TABLE IF NOT EXISTS buildings
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    coordinate COORDINATE,
    address    ADDRESS,
    unit_name  VARCHAR(255) REFERENCES units (name) ON DELETE SET NULL ON UPDATE CASCADE,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    UNIQUE (name, unit_name)
);
COMMENT ON TABLE buildings IS 'Сооружения';

COMMENT ON COLUMN buildings.id IS 'ID';
COMMENT ON COLUMN buildings.name IS 'Наименование';
COMMENT ON COLUMN buildings.unit_name IS 'Наименование воинской части';
COMMENT ON COLUMN buildings.coordinate IS 'Координаты';
COMMENT ON COLUMN buildings.address IS 'Адрес';
COMMENT ON COLUMN buildings.created_at IS 'Время создания';
COMMENT ON COLUMN buildings.updated_at IS 'Время последнего изменения';