CREATE TABLE IF NOT EXISTS armies_brigades
(
    army_id    INTEGER NOT NULL REFERENCES armies (id) ON DELETE CASCADE ON UPDATE CASCADE,
    brigade_id INTEGER NOT NULL REFERENCES brigades (id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (army_id, brigade_id)
);

COMMENT ON TABLE armies_brigades IS 'Бригады в армии';

COMMENT ON COLUMN armies_brigades.army_id IS 'ID армии';
COMMENT ON COLUMN armies_brigades.brigade_id IS 'ID бригады';