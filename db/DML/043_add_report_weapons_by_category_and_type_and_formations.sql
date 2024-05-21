INSERT INTO reports(name, description, query, parameters)
VALUES ('Вооружение по категориям, типам и формированиям',
        'Получить данные о наличии вооружения в целом и с учетом указанной категории или вида во всех частях военного округа, в отдельной армии, дивизии, корпусе, военной части',
        'WITH formation_weapons AS (SELECT ce.serial_number AS serial_number,
                                           cet.name         AS weapon_type_name,
                                           cec.name         AS weapon_category_name,
                                           ce.unit_name     AS unit_name
                                    FROM weapons ce
                                           LEFT JOIN units u               ON u.name = ce.unit_name
                                           LEFT JOIN weapon_types cet      ON cet.id = ce.type_id
                                           LEFT JOIN weapon_categories cec ON cec.id = cet.category_id)
        SELECT DISTINCT fce.serial_number                                                                     AS serial_number,
                        fce.weapon_type_name                                                                  AS weapon_type,
                        fce.weapon_category_name                                                              AS weapon_category,
                        u.name                                                                                AS unit_name,
                        NULLIF(ARRAY_TO_STRING(ARRAY_REMOVE(ARRAY_AGG(DISTINCT b.name), NULL), '', ''), '''') AS brigade_names,
                        NULLIF(ARRAY_TO_STRING(ARRAY_REMOVE(ARRAY_AGG(DISTINCT c.name), NULL), '', ''), '''') AS corps_names,
                        NULLIF(ARRAY_TO_STRING(ARRAY_REMOVE(ARRAY_AGG(DISTINCT d.name), NULL), '', ''), '''') AS division_names,
                        NULLIF(ARRAY_TO_STRING(ARRAY_REMOVE(ARRAY_AGG(DISTINCT a.name), NULL), '', ''), '''') AS army_names
        FROM units u
                 LEFT JOIN formation_weapons fce ON fce.unit_name = u.name
                 LEFT JOIN brigades_units bu     ON bu.unit_id = u.id
                 LEFT JOIN corps_units cu        ON cu.unit_id = u.id
                 LEFT JOIN divisions_units du    ON du.unit_id = u.id
                 LEFT JOIN armies_brigades ab    ON ab.brigade_id = bu.brigade_id
                 LEFT JOIN armies_corps ac       ON ac.corps_id = cu.corps_id
                 LEFT JOIN armies_divisions ad   ON ad.division_id = du.division_id
                 LEFT JOIN brigades b            ON b.id = bu.brigade_id
                 LEFT JOIN corps c               ON c.id = cu.corps_id
                 LEFT JOIN divisions d           ON d.id = du.division_id
                 LEFT JOIN armies a              ON (a.id = ab.army_id OR a.id = ac.army_id OR a.id = ad.army_id)
        WHERE (:weaponCategoryName::VARCHAR IS NULL OR fce.weapon_category_name = :weaponCategoryName::VARCHAR)
          AND (:weaponTypeName::VARCHAR IS NULL     OR fce.weapon_type_name = :weaponTypeName::VARCHAR)
          AND (:unitName::VARCHAR IS NULL           OR u.name = :unitName::VARCHAR)
          AND (:brigadeName::VARCHAR IS NULL        OR b.name = :brigadeName::VARCHAR)
          AND (:corpsName::VARCHAR IS NULL          OR c.name = :corpsName::VARCHAR)
          AND (:divisionName::VARCHAR IS NULL       OR d.name = :divisionName::VARCHAR)
          AND (:armyName::VARCHAR IS NULL           OR a.name = :armyName::VARCHAR)
        GROUP BY fce.serial_number, fce.weapon_type_name, fce.weapon_category_name, u.name
        ORDER BY fce.serial_number',
        ARRAY [
            ('weaponCategoryName', 'SELECT name FROM weapon_categories')::REPORT_PARAMETER,
            ('weaponTypeName', 'SELECT name FROM weapon_types')::REPORT_PARAMETER,
            ('unitName', 'SELECT name FROM units')::REPORT_PARAMETER,
            ('brigadeName', 'SELECT name FROM brigades')::REPORT_PARAMETER,
            ('corpsName', 'SELECT name FROM corps')::REPORT_PARAMETER,
            ('divisionName', 'SELECT name FROM divisions')::REPORT_PARAMETER,
            ('armyName', 'SELECT name FROM armies')::REPORT_PARAMETER])
ON CONFLICT DO NOTHING;