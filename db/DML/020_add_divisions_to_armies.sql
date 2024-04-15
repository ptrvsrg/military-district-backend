CREATE OR REPLACE FUNCTION get_random_division_id() RETURNS INTEGER AS
$$
DECLARE
    random_division_id INTEGER;
BEGIN
    SELECT d.id
    INTO random_division_id
    FROM divisions d
    ORDER BY RANDOM()
    LIMIT 1;

    RETURN random_division_id;
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
                        INSERT INTO armies_divisions (army_id, division_id)
                        VALUES (army_id, get_random_division_id())
                        ON CONFLICT DO NOTHING;
                    END LOOP;
            END LOOP;
    END
$$;

DROP FUNCTION get_random_division_id;
