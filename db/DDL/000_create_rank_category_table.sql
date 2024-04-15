CREATE TABLE IF NOT EXISTS rank_categories
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

COMMENT ON TABLE rank_categories IS 'Категории воинских званий';

COMMENT ON COLUMN rank_categories.id IS 'ID';
COMMENT ON COLUMN rank_categories.name IS 'Название';