CREATE OR REPLACE FUNCTION gen_report_equipments_by_category_and_type_and_formations(
    combat_equipment_category_name VARCHAR(255),
    combat_equipment_type_name VARCHAR(255),
    unit_name VARCHAR(255),
    brigade_name VARCHAR(255),
    corps_name VARCHAR(255),
    division VARCHAR(255),
    army_name VARCHAR(255)
)
    RETURNS TABLE
            (
                SERIAL_NUMBER      VARCHAR(255),
                EQUIPMENT_TYPE     VARCHAR(255),
                EQUIPMENT_CATEGORY VARCHAR(255)
            )
AS
$body$
WITH formation_combat_equipments AS (SELECT ce.serial_number AS serial_number,
                                            cet.name         AS combat_equipment_type_name,
                                            cec.name         AS combat_equipment_category_name,
                                            ce.unit_name     AS unit_name
                                     FROM combat_equipments ce
                                              LEFT JOIN units u ON u.name = ce.unit_name
                                              LEFT JOIN combat_equipment_types cet ON cet.id = ce.type_id
                                              LEFT JOIN combat_equipment_categories cec ON cec.id = cet.category_id)
SELECT DISTINCT fce.serial_number                  AS serial_number,
                fce.combat_equipment_type_name     AS equipment_type,
                fce.combat_equipment_category_name AS equipment_category
FROM units u
         LEFT JOIN formation_combat_equipments fce ON fce.unit_name = u.name
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
WHERE ($1 IS NULL OR fce.combat_equipment_category_name = $1)
  AND ($2 IS NULL OR fce.combat_equipment_type_name = $2)
  AND ($3 IS NULL OR u.name = $3)
  AND ($4 IS NULL OR b.name = $4)
  AND ($5 IS NULL OR c.name = $5)
  AND ($6 IS NULL OR d.name = $6)
  AND ($7 IS NULL OR a.name = $7)
ORDER BY fce.serial_number
$body$
    LANGUAGE sql;