CREATE TABLE IF NOT EXISTS divisions_units
(
    division_id INTEGER NOT NULL REFERENCES divisions (id) ON DELETE CASCADE,
    unit_id     INTEGER NOT NULL REFERENCES units (id) ON DELETE CASCADE,
    PRIMARY KEY (division_id, unit_id)
);

COMMENT ON TABLE divisions_units IS 'Воинские части в дивизии';

COMMENT ON COLUMN divisions_units.division_id IS 'ID дивизии';
COMMENT ON COLUMN divisions_units.unit_id IS 'ID воинской части';