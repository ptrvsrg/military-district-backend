CREATE OR REPLACE FUNCTION get_random_military_mbn() RETURNS VARCHAR(255) AS
$$
DECLARE
    random_military_mbn VARCHAR(255);
BEGIN
    SELECT m.mbn
    INTO random_military_mbn
    FROM militaries m
    WHERE m.rank_id NOTNULL
    ORDER BY RANDOM()
    LIMIT 1;

    RETURN random_military_mbn;
END;
$$ LANGUAGE plpgsql;

INSERT INTO squads(name, commander_mbn, platoon_id, created_at, updated_at)
SELECT ROW_NUMBER() OVER () || '-е отделение' AS name,
       get_random_military_mbn()              AS commander_mbn,
       p.platoon_id                           AS platoon_id,
       NOW()                                  AS created_at,
       NOW()                                  AS updated_at
FROM (SELECT p.id AS platoon_id
      FROM platoons p
      UNION ALL
      SELECT p.id AS platoon_id
      FROM platoons p
      UNION ALL
      SELECT p.id AS platoon_id
      FROM platoons p) p
ON CONFLICT DO NOTHING;

DROP FUNCTION get_random_military_mbn;
