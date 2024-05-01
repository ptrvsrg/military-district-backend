CREATE OR REPLACE FUNCTION gen_report_specialties_by_specialist_count_and_formations(
    unit_name VARCHAR(255),
    brigade_name VARCHAR(255),
    corps_name VARCHAR(255),
    division_name VARCHAR(255),
    army_name VARCHAR(255),
    min_specialist_count INTEGER,
    max_specialist_count INTEGER
)
    RETURNS TABLE
            (
                SPECIALTY_CODE   VARCHAR(7),
                SPECIALTY_NAME   VARCHAR(255),
                SPECIALIST_COUNT INTEGER
            )
AS
$body$
WITH specialists AS (SELECT DISTINCT m.mbn       AS specialist_mbn,
                                     m.unit_name AS unit_name,
                                     s.code      AS specialty_code,
                                     s.name      AS specialty_name
                     FROM specialties s
                              LEFT JOIN militaries_specialties ms ON ms.specialty_id = s.id
                              LEFT JOIN militaries m ON m.id = ms.military_id)
SELECT s.specialty_code                 AS specialty_code,
       s.specialty_name                 AS specialty_name,
       COUNT(DISTINCT s.specialist_mbn) AS specialist_count
FROM specialists s
         LEFT JOIN units u ON u.name = s.unit_name
         LEFT JOIN brigades_units bu ON bu.unit_id = u.id
         LEFT JOIN corps_units cu ON cu.unit_id = u.id
         LEFT JOIN divisions_units du ON du.unit_id = u.id
         LEFT JOIN armies_brigades ab ON ab.brigade_id = bu.brigade_id
         LEFT JOIN armies_corps ac ON ac.corps_id = cu.corps_id
         LEFT JOIN armies_divisions ad ON ad.division_id = du.division_id
         LEFT JOIN brigades b ON b.id = bu.brigade_id
         LEFT JOIN corps c ON c.id = cu.corps_id
         LEFT JOIN divisions d ON d.id = du.division_id
         LEFT JOIN armies a ON (a.id = ab.army_id OR a.id = ac.army_id OR a.id = ad.army_id)
WHERE ($1 IS NULL OR u.name = $1)
  AND ($2 IS NULL OR b.name = $2)
  AND ($3 IS NULL OR c.name = $3)
  AND ($4 IS NULL OR d.name = $4)
  AND ($5 IS NULL OR a.name = $5)
GROUP BY s.specialty_code, s.specialty_name
HAVING (COUNT(DISTINCT s.specialist_mbn) >= $6 OR $6 IS NULL)
   AND (COUNT(DISTINCT s.specialist_mbn) <= $7 OR $7 IS NULL)
ORDER BY specialist_count DESC;
$body$
    LANGUAGE sql;