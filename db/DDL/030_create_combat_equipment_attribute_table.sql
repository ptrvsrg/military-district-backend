CREATE TABLE IF NOT EXISTS combat_equipment_attributes
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    value   VARCHAR(255) NOT NULL,
    type_id INTEGER      NOT NULL REFERENCES combat_equipment_types (id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE (name, type_id)
);

COMMENT ON TABLE combat_equipment_attributes IS 'Атрибуты боевой техники';

COMMENT ON COLUMN combat_equipment_attributes.id IS 'ID';
COMMENT ON COLUMN combat_equipment_attributes.name IS 'Название';
COMMENT ON COLUMN combat_equipment_attributes.value IS 'Значение';
COMMENT ON COLUMN combat_equipment_attributes.type_id IS 'ID вида боевой техники';