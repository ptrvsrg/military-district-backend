CREATE OR REPLACE FUNCTION get_random_corps_id() RETURNS INTEGER AS
$$
DECLARE
    random_corps_id INTEGER;
BEGIN
    SELECT c.id
    INTO random_corps_id
    FROM corps c
    ORDER BY RANDOM()
    LIMIT 1;

    RETURN random_corps_id;
END;
$$ LANGUAGE plpgsql;

DO
$$
    DECLARE
        army_count INTEGER;
        army_id    INTEGER;
    BEGIN
        SELECT COUNT(*)
        INTO army_count
        FROM armies a;

        FOR i IN 1..army_count
            LOOP
                SELECT id
                INTO army_id
                FROM armies a
                WHERE a.name = i || '-я армия';

                FOR j IN 1..5
                    LOOP
                        INSERT INTO armies_corps (army_id, corps_id)
                        VALUES (army_id, get_random_corps_id())
                        ON CONFLICT DO NOTHING;
                    END LOOP;
            END LOOP;
    END
$$;

DROP FUNCTION get_random_corps_id;
