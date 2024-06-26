CREATE TABLE IF NOT EXISTS buildings_platoons
(
    building_id  INTEGER      NOT NULL REFERENCES buildings (id) ON DELETE CASCADE ON UPDATE CASCADE,
    platoon_name VARCHAR(255) NOT NULL REFERENCES platoons (name) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (building_id, platoon_name)
);

COMMENT ON TABLE buildings_platoons IS 'Сооружения взводов';

COMMENT ON COLUMN buildings_platoons.building_id IS 'ID сооружения';
COMMENT ON COLUMN buildings_platoons.platoon_name IS 'Наименование взводы';