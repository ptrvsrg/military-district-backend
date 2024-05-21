CREATE TABLE IF NOT EXISTS combat_equipment_categories
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

COMMENT ON TABLE combat_equipment_categories IS 'Категории боевой техники';

COMMENT ON COLUMN combat_equipment_categories.id IS 'ID';
COMMENT ON COLUMN combat_equipment_categories.name IS 'Наименование';