INSERT INTO reports(name, description, query, parameters)
VALUES ('Воинские части по количеству вооружения',
        'Получить перечень военных частей, в которых число единиц указанного вида вооружения больше 10 (нет указанного вооружения)',
        'SELECT u.name                                                                                 AS unit_name,
                NULLIF(ARRAY_TO_STRING(ARRAY_REMOVE(ARRAY_AGG(DISTINCT wt.name), NULL), '', ''), '''') AS weapon_type_names,
                COUNT(DISTINCT w.serial_number)                                                        AS weapon_count
        FROM units u
                 LEFT JOIN weapons w       ON w.unit_name = u.name
                 LEFT JOIN weapon_types wt ON wt.id = w.type_id
        WHERE (wt.name = :weaponTypeName::VARCHAR OR :weaponTypeName::VARCHAR IS NULL)
        GROUP BY u.name
        HAVING (COUNT(DISTINCT w.serial_number) >= :minWeaponCount::INTEGER OR :minWeaponCount::INTEGER IS NULL)
           AND (COUNT(DISTINCT w.serial_number) <= :maxWeaponCount::INTEGER OR :maxWeaponCount::INTEGER IS NULL)
        ORDER BY weapon_count DESC',
        '{weaponTypeName,minWeaponCount,maxWeaponCount}')
ON CONFLICT DO NOTHING;