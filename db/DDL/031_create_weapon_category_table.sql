CREATE TABLE IF NOT EXISTS weapon_categories
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

COMMENT ON TABLE weapon_categories IS 'Категории вооружения';

COMMENT ON COLUMN weapon_categories.id IS 'ID';
COMMENT ON COLUMN weapon_categories.name IS 'Наименование';