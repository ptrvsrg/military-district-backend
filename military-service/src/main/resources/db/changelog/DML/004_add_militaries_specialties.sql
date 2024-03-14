CREATE OR REPLACE FUNCTION get_random_specialty_id() RETURNS INTEGER AS
$$
DECLARE
    random_specialty_id INTEGER;
BEGIN
    SELECT s.id
    INTO random_specialty_id
    FROM military_service.specialties s
    ORDER BY RANDOM()
    LIMIT 1;

    RETURN random_specialty_id;
END;
$$ LANGUAGE plpgsql;

DO
$$
    DECLARE
        military_count INTEGER;
        military_id    INTEGER;
    BEGIN
        SELECT COUNT(*)
        INTO military_count
        FROM military_service.militaries;

        FOR i IN 1..military_count
            LOOP
                SELECT id
                INTO military_id
                FROM military_service.militaries
                WHERE mbn = 'ВС России Э-' || LPAD(i::TEXT, 6, '0');

                FOR j IN 1..5
                    LOOP
                        INSERT INTO military_service.militaries_specialties (military_id, specialty_id)
                        VALUES (military_id, get_random_specialty_id())
                        ON CONFLICT DO NOTHING;
                    END LOOP;
            END LOOP;
    END
$$;

DROP FUNCTION get_random_specialty_id;