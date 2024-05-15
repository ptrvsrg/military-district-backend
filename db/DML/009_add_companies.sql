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

INSERT INTO companies(name, commander_mbn, unit_id)
SELECT ROW_NUMBER() OVER () || '-я рота' AS name,
       get_random_officer_mbn()          AS commander_mbn,
       u.unit_id                         AS unit_id
FROM (SELECT u.id AS unit_id
      FROM units u
      UNION ALL
      SELECT u.id AS unit_id
      FROM units u
      UNION ALL
      SELECT u.id AS unit_id
      FROM units u) u
ON CONFLICT DO NOTHING;

DROP FUNCTION get_random_officer_mbn;
