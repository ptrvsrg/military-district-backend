CREATE TABLE IF NOT EXISTS ranks
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL UNIQUE,
    level            INTEGER      NOT NULL,
    rank_category_id INTEGER      NOT NULL REFERENCES rank_categories (id) ON DELETE SET NULL
);

COMMENT ON TABLE ranks IS 'Воинские звания';

COMMENT ON COLUMN ranks.id IS 'ID';
COMMENT ON COLUMN ranks.name IS 'Наименование';
COMMENT ON COLUMN ranks.level IS 'Уровень в военной иерархии';
COMMENT ON COLUMN ranks.rank_category_id IS 'ID категории воинских званий';