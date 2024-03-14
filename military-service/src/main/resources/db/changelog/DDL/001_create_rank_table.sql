CREATE TABLE IF NOT EXISTS military_service.ranks
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL UNIQUE,
    level            INTEGER      NOT NULL,
    rank_category_id INTEGER      NOT NULL REFERENCES military_service.rank_categories (id) ON DELETE SET NULL,
    created_at       TIMESTAMPTZ  NOT NULL,
    updated_at       TIMESTAMPTZ  NOT NULL
);

COMMENT ON TABLE military_service.ranks IS 'Воинские звания';

COMMENT ON COLUMN military_service.ranks.id IS 'ID';
COMMENT ON COLUMN military_service.ranks.name IS 'Наименование';
COMMENT ON COLUMN military_service.ranks.level IS 'Уровень в военной иерархии';
COMMENT ON COLUMN military_service.ranks.rank_category_id IS 'ID категории воинских званий';
COMMENT ON COLUMN military_service.ranks.created_at IS 'Время создания';
COMMENT ON COLUMN military_service.ranks.updated_at IS 'Время последнего изменения';