CREATE OR REPLACE FUNCTION gen_report_units_by_equipment_count(
    combat_equipment_type_name VARCHAR(255),
    min_combat_equipment_count INTEGER,
    max_combat_equipment_count INTEGER
)
    RETURNS TABLE
            (
                UNIT_NAME       VARCHAR(255),
                EQUIPMENT_COUNT INTEGER
            )
AS
$body$
SELECT u.name                           AS unit_name,
       COUNT(DISTINCT ce.serial_number) AS equipment_count
FROM units u
         LEFT JOIN combat_equipments ce ON ce.unit_name = u.name
         LEFT JOIN combat_equipment_types cet ON cet.id = ce.type_id
WHERE (cet.name = $1 OR $1 IS NULL)
GROUP BY u.name
HAVING (COUNT(DISTINCT ce.serial_number) >= $2 OR $2 IS NULL)
   AND (COUNT(DISTINCT ce.serial_number) <= $3 OR $3 IS NULL)
ORDER BY equipment_count DESC
$body$
    LANGUAGE sql;