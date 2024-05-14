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

INSERT INTO platoons(name, commander_mbn, company_id)
SELECT ROW_NUMBER() OVER () || '-й взвод' AS name,
       get_random_military_mbn()          AS commander_mbn,
       c.company_id                       AS company_id
FROM (SELECT c.id AS company_id
      FROM companies c
      UNION ALL
      SELECT c.id AS company_id
      FROM companies c
      UNION ALL
      SELECT c.id AS company_id
      FROM companies c) c
ON CONFLICT DO NOTHING;

DROP FUNCTION get_random_military_mbn;
