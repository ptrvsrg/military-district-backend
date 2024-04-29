CREATE OR REPLACE FUNCTION gen_report_weapons_by_category_and_type_and_formations(
    weapon_category_name VARCHAR(255),
    weapon_type_name VARCHAR(255),
    army_name VARCHAR(255),
    brigade_name VARCHAR(255),
    corps_name VARCHAR(255),
    division_name VARCHAR(255),
    unit_name VARCHAR(255)
)
    RETURNS TABLE
            (
                SERIAL_NUMBER   VARCHAR(255),
                WEAPON_TYPE     VARCHAR(255),
                WEAPON_CATEGORY VARCHAR(255)
            )
AS
$body$
WITH formation_weapons AS (SELECT w.serial_number AS serial_number,
                                  wt.name         AS weapon_type_name,
                                  wc.name         AS weapon_category_name,
                                  w.unit_name     AS unit_name
                           FROM weapons w
                                    LEFT JOIN units u ON u.name = w.unit_name
                                    LEFT JOIN weapon_types wt ON wt.id = w.type_id
                                    LEFT JOIN weapon_categories wc ON wc.id = wt.category_id)
SELECT DISTINCT fw.serial_number        AS serial_number,
                fw.weapon_type_name     AS weapon_type,
                fw.weapon_category_name AS weapon_category
FROM units u
         LEFT JOIN formation_weapons fw ON fw.unit_name = u.name
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
WHERE ($1 IS NULL OR fw.weapon_category_name = $1)
  AND ($2 IS NULL OR fw.weapon_type_name = $2)
  AND ($3 IS NULL OR a.name = $3)
  AND ($4 IS NULL OR b.name = $4)
  AND ($5 IS NULL OR c.name = $5)
  AND ($6 IS NULL OR d.name = $6)
  AND ($7 IS NULL OR u.name = $7)
ORDER BY fw.serial_number
$body$
    LANGUAGE sql;