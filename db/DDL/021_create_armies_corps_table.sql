CREATE TABLE IF NOT EXISTS armies_corps
(
    army_id  INTEGER NOT NULL REFERENCES armies (id) ON DELETE CASCADE,
    corps_id INTEGER NOT NULL REFERENCES corps (id) ON DELETE CASCADE,
    PRIMARY KEY (army_id, corps_id)
);

COMMENT ON TABLE armies_corps IS 'Корпусы в армии';
COMMENT ON COLUMN armies_corps.army_id IS 'ID армии';
COMMENT ON COLUMN armies_corps.corps_id IS 'ID корпуса';