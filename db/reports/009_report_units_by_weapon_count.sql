CREATE OR REPLACE FUNCTION gen_report_units_by_weapon_count(
    weapon_type_name VARCHAR(255),
    min_weapon_count INTEGER,
    max_weapon_count INTEGER
)
    RETURNS TABLE
            (
                UNIT_NAME       VARCHAR(255),
                EQUIPMENT_COUNT INTEGER
            )
AS
$body$
SELECT u.name                          AS unit_name,
       COUNT(DISTINCT w.serial_number) AS equipment_count
FROM units u
         LEFT JOIN weapons w ON w.unit_name = u.name
         LEFT JOIN weapon_types wt ON wt.id = w.type_id
WHERE (wt.name = $1 OR $1 IS NULL)
GROUP BY u.name
HAVING (COUNT(DISTINCT w.serial_number) >= $2 OR $2 IS NULL)
   AND (COUNT(DISTINCT w.serial_number) <= $3 OR $3 IS NULL)
ORDER BY equipment_count DESC
$body$
    LANGUAGE sql;