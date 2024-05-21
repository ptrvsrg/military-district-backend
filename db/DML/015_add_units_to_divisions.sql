CREATE OR REPLACE FUNCTION get_random_unit_id() RETURNS INTEGER AS
$$
DECLARE
    random_unit_id INTEGER;
BEGIN
    SELECT u.id
    INTO random_unit_id
    FROM units u
    ORDER BY RANDOM()
    LIMIT 1;

    RETURN random_unit_id;
END;
$$ LANGUAGE plpgsql;

DO
$$
    DECLARE
        division_count INTEGER;
        division_id    INTEGER;
    BEGIN
        SELECT COUNT(*)
        INTO division_count
        FROM divisions d;

        FOR i IN 1..division_count
            LOOP
                SELECT id
                INTO division_id
                FROM divisions d
                WHERE d.name = i || '-я дивизия';

                FOR j IN 1..5
                    LOOP
                        INSERT INTO divisions_units (division_id, unit_id)
                        VALUES (division_id, get_random_unit_id())
                        ON CONFLICT DO NOTHING;
                    END LOOP;
            END LOOP;
    END
$$;

DROP FUNCTION get_random_unit_id;
