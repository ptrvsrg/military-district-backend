CREATE OR REPLACE FUNCTION get_random_building_id(_unit_id INTEGER) RETURNS INTEGER AS
$$
DECLARE
    random_building_id INTEGER;
BEGIN
    SELECT b.id
    INTO random_building_id
    FROM buildings b
             JOIN units u ON u.name = b.unit_name
    WHERE u.id = _unit_id
    ORDER BY RANDOM()
    LIMIT 1;

    RETURN random_building_id;
END;
$$ LANGUAGE plpgsql;

INSERT
INTO buildings_squads(building_id, squad_name)
SELECT get_random_building_id(c.unit_id) AS building_id,
       s.name                            AS squad_name
FROM squads s
         JOIN platoons p ON p.id = s.platoon_id
         JOIN companies c ON c.id = p.company_id
ON CONFLICT DO NOTHING;

DROP FUNCTION get_random_building_id;