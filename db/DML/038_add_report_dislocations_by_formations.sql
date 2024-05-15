INSERT INTO reports(name, description, query, parameters)
VALUES ('Места дислокации по формированиям',
        'Получить перечень мест дислокации всех частей военного округа, отдельной армии, дивизии, корпуса, военной части',
        'SELECT DISTINCT
                u.coordinate                                                                          AS coordinate,
                u.address                                                                             AS address,
                u.name                                                                                AS unit_name,
                NULLIF(ARRAY_TO_STRING(ARRAY_REMOVE(ARRAY_AGG(DISTINCT b.name), NULL), '', ''), '''') AS brigade_names,
                NULLIF(ARRAY_TO_STRING(ARRAY_REMOVE(ARRAY_AGG(DISTINCT c.name), NULL), '', ''), '''') AS corps_names,
                NULLIF(ARRAY_TO_STRING(ARRAY_REMOVE(ARRAY_AGG(DISTINCT d.name), NULL), '', ''), '''') AS division_names,
                NULLIF(ARRAY_TO_STRING(ARRAY_REMOVE(ARRAY_AGG(DISTINCT a.name), NULL), '', ''), '''') AS army_names
        FROM units u
                 LEFT JOIN brigades_units bu   ON bu.unit_id = u.id
                 LEFT JOIN corps_units cu      ON cu.unit_id = u.id
                 LEFT JOIN divisions_units du  ON du.unit_id = u.id
                 LEFT JOIN armies_brigades ab  ON ab.brigade_id = bu.brigade_id
                 LEFT JOIN armies_corps ac     ON ac.corps_id = cu.corps_id
                 LEFT JOIN armies_divisions ad ON ad.division_id = du.division_id
                 LEFT JOIN brigades b          ON b.id = bu.brigade_id
                 LEFT JOIN corps c             ON c.id = cu.corps_id
                 LEFT JOIN divisions d         ON d.id = du.division_id
                 LEFT JOIN armies a            ON (a.id = ab.army_id OR a.id = ac.army_id OR a.id = ad.army_id)
        WHERE (:unitName::VARCHAR IS NULL     OR u.name = :unitName::VARCHAR)
          AND (:brigadeName::VARCHAR IS NULL  OR b.name = :brigadeName::VARCHAR)
          AND (:corpsName::VARCHAR IS NULL    OR c.name = :corpsName::VARCHAR)
          AND (:divisionName::VARCHAR IS NULL OR d.name = :divisionName::VARCHAR)
          AND (:armyName::VARCHAR IS NULL     OR a.name = :armyName::VARCHAR)
        GROUP BY u.coordinate, u.address, u.name',
        '{unitName,brigadeName,corpsName,divisionName,armyName}')
ON CONFLICT DO NOTHING;