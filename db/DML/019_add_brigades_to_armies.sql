CREATE OR REPLACE FUNCTION get_random_brigade_id() RETURNS INTEGER AS
$$
DECLARE
    random_brigade_id INTEGER;
BEGIN
    SELECT b.id
    INTO random_brigade_id
    FROM brigades b
    ORDER BY RANDOM()
    LIMIT 1;

    RETURN random_brigade_id;
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
                        INSERT INTO armies_brigades (army_id, brigade_id)
                        VALUES (army_id, get_random_brigade_id())
                        ON CONFLICT DO NOTHING;
                    END LOOP;
            END LOOP;
    END
$$;

DROP FUNCTION get_random_brigade_id;
