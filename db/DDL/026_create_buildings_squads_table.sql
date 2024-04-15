CREATE TABLE IF NOT EXISTS buildings_squads
(
    building_id INTEGER      NOT NULL REFERENCES buildings (id),
    squad_name  VARCHAR(255) NOT NULL REFERENCES squads (name),
    PRIMARY KEY (building_id, squad_name)
);

COMMENT ON TABLE buildings_squads IS 'Сооружения отделений';

COMMENT ON COLUMN buildings_squads.building_id IS 'ID сооружения';
COMMENT ON COLUMN buildings_squads.squad_name IS 'Наименование отделения';