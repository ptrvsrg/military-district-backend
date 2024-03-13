CREATE TABLE IF NOT EXISTS military_attributes
(
    id                      SERIAL PRIMARY KEY,
    military_id             INTEGER NOT NULL REFERENCES militaries (id) ON DELETE CASCADE,
    attribute_definition_id INTEGER NOT NULL REFERENCES military_attribute_definitions (id) ON DELETE CASCADE,
    value                   VARCHAR(255),
    UNIQUE (military_id, attribute_definition_id)
);

COMMENT ON TABLE military_attributes IS 'Атрибуты военнослужащего';

COMMENT ON COLUMN military_attributes.id IS 'ID';
COMMENT ON COLUMN military_attributes.military_id IS 'ID военнослужащего';
COMMENT ON COLUMN military_attributes.attribute_definition_id IS 'ID атрибута';
COMMENT ON COLUMN military_attributes.value IS 'Значение';