CREATE TABLE IF NOT EXISTS weapon_attributes
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    value   VARCHAR(255) NOT NULL,
    type_id INTEGER      NOT NULL REFERENCES weapon_types (id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE (name, type_id)
);

COMMENT ON TABLE weapon_attributes IS 'Атрибуты вооружения';

COMMENT ON COLUMN weapon_attributes.id IS 'ID';
COMMENT ON COLUMN weapon_attributes.name IS 'Название';
COMMENT ON COLUMN weapon_attributes.value IS 'Значение';
COMMENT ON COLUMN weapon_attributes.type_id IS 'ID вида боевой техники';