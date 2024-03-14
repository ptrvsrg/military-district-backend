CREATE TABLE IF NOT EXISTS military_service.military_attribute_definitions
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    rank_id     INTEGER      NOT NULL REFERENCES military_service.ranks (id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    UNIQUE (name, rank_id)
);

COMMENT ON TABLE military_service.military_attribute_definitions IS 'Атрибуты военнослужащих, определяемые воинским званием';

COMMENT ON COLUMN military_service.military_attribute_definitions.id IS 'ID';
COMMENT ON COLUMN military_service.military_attribute_definitions.name IS 'Название';
COMMENT ON COLUMN military_service.military_attribute_definitions.description IS 'Описание';
COMMENT ON COLUMN military_service.military_attribute_definitions.rank_id IS 'ID воинского звания, которому принадлежит атрибут';
COMMENT ON COLUMN military_service.military_attribute_definitions.created_at IS 'Время создания';
COMMENT ON COLUMN military_service.military_attribute_definitions.updated_at IS 'Время последнего изменения';