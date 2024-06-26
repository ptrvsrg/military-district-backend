CREATE OR REPLACE FUNCTION get_random_officer_mbn() RETURNS VARCHAR(255) AS
$$
DECLARE
    random_officer_mbn VARCHAR(255);
BEGIN
    SELECT m.mbn
    INTO random_officer_mbn
    FROM militaries m
             JOIN ranks r ON r.id = m.rank_id
             JOIN rank_categories rc ON rc.id = r.rank_category_id
    WHERE rc.name = 'Офицерский состав'
    ORDER BY RANDOM()
    LIMIT 1;

    RETURN random_officer_mbn;
END;
$$ LANGUAGE plpgsql;

DO
$$
    BEGIN
        FOR i IN 1..4
            LOOP
                INSERT INTO armies(name, commander_mbn)
                SELECT i || '-я армия'          AS name,
                       get_random_officer_mbn() AS commander_mbn
                ON CONFLICT DO NOTHING;
            END LOOP;
    END
$$;

DROP FUNCTION get_random_officer_mbn;
