CREATE TABLE IF NOT EXISTS combat_equipment_types
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    category_id INTEGER      NOT NULL REFERENCES combat_equipment_categories (id),
    UNIQUE (name, category_id)
);

COMMENT ON TABLE combat_equipment_types IS 'Виды боевой техники';

COMMENT ON COLUMN combat_equipment_types.id IS 'ID';
COMMENT ON COLUMN combat_equipment_types.name IS 'Наименование';
COMMENT ON COLUMN combat_equipment_types.category_id IS 'ID категории боевой техники';