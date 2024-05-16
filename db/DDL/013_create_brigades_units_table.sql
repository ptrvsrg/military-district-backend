CREATE TABLE IF NOT EXISTS brigades_units
(
    brigade_id INTEGER NOT NULL REFERENCES brigades (id) ON DELETE CASCADE ON UPDATE CASCADE,
    unit_id    INTEGER NOT NULL REFERENCES units (id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (brigade_id, unit_id)
);

COMMENT ON TABLE brigades_units IS 'Воинские части в бригаде';

COMMENT ON COLUMN brigades_units.brigade_id IS 'ID бригады';
COMMENT ON COLUMN brigades_units.unit_id IS 'ID воинской части';