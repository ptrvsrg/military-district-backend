CREATE OR REPLACE FUNCTION gen_report_militaries_by_specialty_and_formations(
    specialty_code VARCHAR(7),
    unit_name VARCHAR(255),
    brigade_name VARCHAR(255),
    corps_name VARCHAR(255),
    division_name VARCHAR(255),
    army_name VARCHAR(255)
)
    RETURNS TABLE
            (
                SPECIALIST_MBN  VARCHAR(255),
                SPECIALIST_NAME VARCHAR(255),
                SPECIALTY_CODE  VARCHAR(7),
                SPECIALTY_NAME  VARCHAR(255)
            )
AS
$body$
WITH specialists AS (SELECT DISTINCT m.mbn                         AS specialist_mbn,
                                     CONCAT(m.last_name, ' ', m.first_name,
                                            CASE
                                                WHEN m.middle_name IS NULL THEN ''
                                                ELSE CONCAT(' ', m.middle_name)
                                                END)::VARCHAR(255) AS specialist_name,
                                     m.unit_name                   AS unit_name,
                                     s.code                        AS specialty_code,
                                     s.name                        AS specialty_name
                     FROM specialties s
                              LEFT JOIN militaries_specialties ms ON ms.specialty_id = s.id
                              LEFT JOIN militaries m ON m.id = ms.military_id
                     WHERE ($1 IS NULL OR s.code = $1))
SELECT s.specialist_mbn  AS specialist_mbn,
       s.specialist_name AS specialist_name,
       s.specialty_code  AS specialty_code,
       s.specialty_name  AS specialty_name
FROM specialists s
         LEFT JOIN units u
                   ON u.name = s.unit_name
         LEFT JOIN brigades_units bu ON bu.unit_id = u.id
         LEFT JOIN corps_units cu ON cu.unit_id = u.id
         LEFT JOIN divisions_units du ON du.unit_id = u.id
         LEFT JOIN armies_brigades ab ON ab.brigade_id = bu.brigade_id
         LEFT JOIN armies_corps ac ON ac.corps_id = cu.corps_id
         LEFT JOIN armies_divisions ad ON ad.division_id = du.division_id
         LEFT JOIN brigades b ON b.id = bu.brigade_id
         LEFT JOIN corps c ON c.id = cu.corps_id
         LEFT JOIN divisions d ON d.id = du.division_id
         LEFT JOIN armies a
                   ON (a.id = ab.army_id OR a.id = ac.army_id OR a.id = ad.army_id)
WHERE ($2 IS NULL OR u.name = $2)
  AND ($3 IS NULL OR b.name = $3)
  AND ($4 IS NULL OR c.name = $4)
  AND ($5 IS NULL OR d.name = $5)
  AND ($6 IS NULL OR a.name = $6)
ORDER BY specialist_mbn;
$body$
    LANGUAGE sql;