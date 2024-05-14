INSERT INTO reports(name, description, query, parameters)
VALUES ('Военнослужащие по специальности и формированиям',
        'Получить перечень военнослужащих указанной специальности в округе, в отдельной армии, дивизии, корпусе, военной части, в указанном подразделении некоторой военной части',
        'WITH specialists AS (SELECT DISTINCT m.mbn                         AS specialist_mbn,
                                             CONCAT(m.last_name, '' '', m.first_name,
                                                    CASE
                                                        WHEN m.middle_name IS NULL THEN ''
                                                        ELSE CONCAT('' '', m.middle_name)
                                                        END)::VARCHAR(255) AS specialist_name,
                                             m.unit_name                   AS unit_name,
                                             s.code                        AS specialty_code,
                                             s.name                        AS specialty_name
                             FROM specialties s
                                      LEFT JOIN militaries_specialties ms ON ms.specialty_id = s.id
                                      LEFT JOIN militaries m ON m.id = ms.military_id
                             WHERE (:specialtyCode::VARCHAR IS NULL OR s.code = :specialtyCode::VARCHAR))
        SELECT s.specialist_mbn  AS specialist_mbn,
               s.specialist_name AS specialist_name,
               s.specialty_code  AS specialty_code,
               s.specialty_name  AS specialty_name
        FROM specialists s
                 LEFT JOIN units u
                           ON u.name = s.unit_name
                 LEFT JOIN brigades_units bu ON bu.unit_id = u.id
                 LEFT JOIN corps_units cu ON cu.unit_id = u.id
                 LEFT JOIN divisions_units du ON du.unit_id = u.id
                 LEFT JOIN armies_brigades ab ON ab.brigade_id = bu.brigade_id
                 LEFT JOIN armies_corps ac ON ac.corps_id = cu.corps_id
                 LEFT JOIN armies_divisions ad ON ad.division_id = du.division_id
                 LEFT JOIN brigades b ON b.id = bu.brigade_id
                 LEFT JOIN corps c ON c.id = cu.corps_id
                 LEFT JOIN divisions d ON d.id = du.division_id
                 LEFT JOIN armies a
                           ON (a.id = ab.army_id OR a.id = ac.army_id OR a.id = ad.army_id)
        WHERE (:unitName::VARCHAR IS NULL OR u.name = :unitName::VARCHAR)
          AND (:brigadeName::VARCHAR IS NULL OR b.name = :brigadeName::VARCHAR)
          AND (:corpsName::VARCHAR IS NULL OR c.name = :corpsName::VARCHAR)
          AND (:divisionName::VARCHAR IS NULL OR d.name = :divisionName::VARCHAR)
          AND (:armyName::VARCHAR IS NULL OR a.name = :armyName::VARCHAR)
        ORDER BY specialist_mbn;',
        '{specialtyCode, unitName, brigadeName, corpsName, divisionName, armyName}')
ON CONFLICT DO NOTHING;