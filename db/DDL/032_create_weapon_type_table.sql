CREATE TABLE IF NOT EXISTS weapon_types
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    category_id INTEGER      NOT NULL REFERENCES weapon_categories (id),
    UNIQUE (name, category_id)
);

COMMENT ON TABLE weapon_types IS 'Вид вооружения';

COMMENT ON COLUMN weapon_types.id IS 'ID';
COMMENT ON COLUMN weapon_types.name IS 'Наименование';
COMMENT ON COLUMN weapon_types.category_id IS 'ID категории вооружения';