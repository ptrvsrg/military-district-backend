CREATE TABLE IF NOT EXISTS corps_units
(
    corps_id INTEGER NOT NULL REFERENCES corps (id) ON DELETE CASCADE ON UPDATE CASCADE,
    unit_id  INTEGER NOT NULL REFERENCES units (id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (corps_id, unit_id)
);

COMMENT ON TABLE corps_units IS 'Воинские части в корпусе';

COMMENT ON COLUMN corps_units.corps_id IS 'ID корпуса';
COMMENT ON COLUMN corps_units.unit_id IS 'ID воинской части';