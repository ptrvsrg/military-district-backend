INSERT INTO reports(name, description, query, parameters)
VALUES ('Боевая техника по категориям, типам и формированиям',
        'Получить данные о наличии боевой технике в целом и с учетом указанной категории или вида во всех частях военного округа, в отдельной армии, дивизии, корпусе, военной части',
        'WITH formation_combat_equipments AS (SELECT ce.serial_number AS serial_number,
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
        WHERE (:combatEquipmentCategoryName::VARCHAR IS NULL OR fce.combat_equipment_category_name = :combatEquipmentCategoryName::VARCHAR)
          AND (:combatEquipmentTypeName::VARCHAR IS NULL OR fce.combat_equipment_type_name = :combatEquipmentTypeName::VARCHAR)
          AND (:unitName::VARCHAR IS NULL OR u.name = :unitName::VARCHAR)
          AND (:brigadeName::VARCHAR IS NULL OR b.name = :brigadeName::VARCHAR)
          AND (:corpsName::VARCHAR IS NULL OR c.name = :corpsName::VARCHAR)
          AND (:divisionName::VARCHAR IS NULL OR d.name = :divisionName::VARCHAR)
          AND (:armyName::VARCHAR IS NULL OR a.name = :armyName::VARCHAR)
        ORDER BY fce.serial_number',
        '{combatEquipmentCategoryName, combatEquipmentTypeName, unitName, brigadeName, corpsName, divisionName, armyName}')