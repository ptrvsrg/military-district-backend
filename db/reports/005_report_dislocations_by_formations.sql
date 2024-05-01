CREATE OR REPLACE FUNCTION gen_report_dislocations_by_formations(
    unit_name VARCHAR(255),
    brigade_name VARCHAR(255),
    corps_name VARCHAR(255),
    division_name VARCHAR(255),
    army_name VARCHAR(255)
)
    RETURNS TABLE
            (
                COORDINATE COORDINATE,
                ADDRESS    ADDRESS
            )
AS
$body$
SELECT DISTINCT u.coordinate AS coordinate,
                u.address    AS address
FROM units u
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
$body$
    LANGUAGE sql;