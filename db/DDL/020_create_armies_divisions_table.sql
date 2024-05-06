CREATE TABLE IF NOT EXISTS armies_divisions
(
    army_id     INTEGER NOT NULL REFERENCES armies (id) ON DELETE CASCADE ON UPDATE CASCADE,
    division_id INTEGER NOT NULL REFERENCES divisions (id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (army_id, division_id)
);

COMMENT ON TABLE armies_divisions IS 'Дивизии в армии';

COMMENT ON COLUMN armies_divisions.army_id IS 'ID армии';
COMMENT ON COLUMN armies_divisions.division_id IS 'ID дивизии';