CREATE OR REPLACE FUNCTION gen_report_unit_buildings_by_squad_count(
    unit_name VARCHAR(255),
    min_squad_count INTEGER,
    max_squad_count INTEGER
)
    RETURNS TABLE
            (
                BUILDING_NAME VARCHAR(255),
                COORDINATE    COORDINATE,
                ADDRESS       ADDRESS,
                UNIT_NAME     VARCHAR(255)
            )
AS
$body$
WITH units_with_squad_count AS (SELECT u.name AS unit_name, COUNT(DISTINCT s.id) AS squad_count
                                FROM units u
                                         LEFT JOIN companies c ON c.unit_id = u.id
                                         LEFT JOIN platoons p ON p.company_id = c.id
                                         LEFT JOIN squads s ON s.platoon_id = p.id
                                GROUP BY u.name)
SELECT b.name         AS building_name,
       b.coordinate   AS coordinate,
       b.address      AS address,
       uwsc.unit_name AS unit_name
FROM buildings b
         LEFT JOIN units_with_squad_count uwsc ON uwsc.unit_name = b.unit_name
WHERE (b.unit_name = $1 OR $1 IS NULL)
  AND (uwsc.squad_count >= $2 OR $2 IS NULL)
  AND (uwsc.squad_count <= $3 OR $3 IS NULL)
$body$
    LANGUAGE sql;