CREATE TABLE IF NOT EXISTS military_attributes
(
    id          SERIAL PRIMARY KEY,
    military_id INTEGER      NOT NULL REFERENCES militaries (id) ON DELETE CASCADE ON UPDATE CASCADE,
    name        VARCHAR(255) NOT NULL,
    rank_id     INTEGER      NOT NULL REFERENCES ranks (id) ON DELETE CASCADE ON UPDATE CASCADE,
    value       VARCHAR(255),
    UNIQUE (military_id, name, rank_id)
);

COMMENT ON TABLE military_attributes IS 'Атрибуты военнослужащего';

COMMENT ON COLUMN military_attributes.id IS 'ID';
COMMENT ON COLUMN military_attributes.military_id IS 'ID военнослужащего';
COMMENT ON COLUMN military_attributes.name IS 'Название';
COMMENT ON COLUMN military_attributes.rank_id IS 'Воинское звание';
COMMENT ON COLUMN military_attributes.value IS 'Значение';