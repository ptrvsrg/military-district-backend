CREATE TABLE IF NOT EXISTS building_attributes
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    value       VARCHAR(255) NOT NULL,
    building_id INTEGER      NOT NULL REFERENCES buildings (id) ON DELETE CASCADE,
    UNIQUE (building_id, name)
);

COMMENT ON TABLE building_attributes IS 'Атрибуты сооружений';

COMMENT ON COLUMN building_attributes.name IS 'Наименование';
COMMENT ON COLUMN building_attributes.value IS 'Значение';
COMMENT ON COLUMN building_attributes.building_id IS 'ID сооружения';