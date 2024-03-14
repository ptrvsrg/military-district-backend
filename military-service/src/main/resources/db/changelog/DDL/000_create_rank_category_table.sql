CREATE SCHEMA IF NOT EXISTS military_service;

CREATE TABLE IF NOT EXISTS military_service.rank_categories
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ  NOT NULL,
    updated_at TIMESTAMPTZ  NOT NULL
);

COMMENT ON TABLE military_service.rank_categories IS 'Категории воинских званий';

COMMENT ON COLUMN military_service.rank_categories.id IS 'ID';
COMMENT ON COLUMN military_service.rank_categories.name IS 'Название';
COMMENT ON COLUMN military_service.rank_categories.created_at IS 'Время создания';
COMMENT ON COLUMN military_service.rank_categories.updated_at IS 'Время последнего изменения';