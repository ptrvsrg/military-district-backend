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
        corps_count INTEGER;
        corps_id    INTEGER;
    BEGIN
        SELECT COUNT(*)
        INTO corps_count
        FROM corps c;

        FOR i IN 1..corps_count
            LOOP
                SELECT id
                INTO corps_id
                FROM corps c
                WHERE c.name = i || '-й корпус';

                FOR j IN 1..5
                    LOOP
                        INSERT INTO corps_units (corps_id, unit_id)
                        VALUES (corps_id, get_random_unit_id())
                        ON CONFLICT DO NOTHING;
                    END LOOP;
            END LOOP;
    END
$$;

DROP FUNCTION get_random_unit_id;
