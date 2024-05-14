CREATE OR REPLACE FUNCTION get_random_unit_name() RETURNS VARCHAR(255) AS
$$
DECLARE
    random_unit_name VARCHAR(255);
BEGIN
    SELECT u.name
    INTO random_unit_name
    FROM units u
    ORDER BY RANDOM()
    LIMIT 1;

    RETURN random_unit_name;
END;
$$ LANGUAGE plpgsql;

DO
$$
    BEGIN
        FOR i IN 1..100
            LOOP
                INSERT
                INTO combat_equipments (serial_number, type_id, unit_name)
                SELECT UPPER(LEFT(TRANSLATE(gen_random_uuid()::TEXT, '-', '')::TEXT, 17)) AS serial_number,
                       cet.id                                                             AS type_id,
                       get_random_unit_name()                                             AS unit_name
                FROM combat_equipment_types cet
                ON CONFLICT DO NOTHING;
            END LOOP;
    END
$$;

DROP FUNCTION get_random_unit_name;