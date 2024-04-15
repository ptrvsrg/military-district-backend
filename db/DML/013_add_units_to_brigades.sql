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
        brigade_count INTEGER;
        brigade_id    INTEGER;
    BEGIN
        SELECT COUNT(*)
        INTO brigade_count
        FROM brigades b;

        FOR i IN 1..brigade_count
            LOOP
                SELECT b.id
                INTO brigade_id
                FROM brigades b
                WHERE b.name = i || '-я бригада';

                FOR j IN 1..5
                    LOOP
                        INSERT INTO brigades_units (brigade_id, unit_id)
                        VALUES (brigade_id, get_random_unit_id())
                        ON CONFLICT DO NOTHING;
                    END LOOP;
            END LOOP;
    END
$$;

DROP FUNCTION get_random_unit_id;
