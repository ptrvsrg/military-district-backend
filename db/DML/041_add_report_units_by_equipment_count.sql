INSERT INTO reports(name, description, query, parameters)
VALUES ('Воинские части по количеству боевой техники указанного вида',
        'Получить перечень военных частей, в которых число единиц указанного вида боевой техники больше 5 (нет указанной боевой техники)',
        'SELECT u.name                                                                                  AS unit_name,
                NULLIF(ARRAY_TO_STRING(ARRAY_REMOVE(ARRAY_AGG(DISTINCT cet.name), NULL), '', ''), '''') AS combat_equipment_types,
                COUNT(DISTINCT ce.serial_number)                                                        AS combat_equipment_count
        FROM units u
                 LEFT JOIN combat_equipments ce       ON ce.unit_name = u.name
                 LEFT JOIN combat_equipment_types cet ON cet.id = ce.type_id
        WHERE (cet.name = :combatEquipmentTypeName::VARCHAR OR :combatEquipmentTypeName::VARCHAR IS NULL)
        GROUP BY u.name
        HAVING (COUNT(DISTINCT ce.serial_number) >= :minCombatEquipmentCount::INTEGER OR :minCombatEquipmentCount::INTEGER IS NULL)
           AND (COUNT(DISTINCT ce.serial_number) <= :maxCombatEquipmentCount::INTEGER OR :maxCombatEquipmentCount::INTEGER IS NULL)
        ORDER BY combat_equipment_count DESC',
        ARRAY [
            ('combatEquipmentTypeName', 'SELECT name FROM combat_equipment_types')::REPORT_PARAMETER,
            ('minCombatEquipmentCount', NULL)::REPORT_PARAMETER,
            ('maxCombatEquipmentCount', NULL)::REPORT_PARAMETER])
ON CONFLICT DO NOTHING;