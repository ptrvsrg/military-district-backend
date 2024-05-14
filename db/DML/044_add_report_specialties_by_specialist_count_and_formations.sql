INSERT INTO reports(name, description, query, parameters)
VALUES ('Военные специальности по количеству специалистов и формированиям',
        'Получить перечень военных специальностей, по которым в округе, в отдельной армии, дивизии, корпусе, военной части более пяти специалистов (нет специалистов)',
        'WITH specialists AS (SELECT DISTINCT m.mbn       AS specialist_mbn,
                                             m.unit_name AS unit_name,
                                             s.code      AS specialty_code,
                                             s.name      AS specialty_name
                             FROM specialties s
                                      LEFT JOIN militaries_specialties ms ON ms.specialty_id = s.id
                                      LEFT JOIN militaries m ON m.id = ms.military_id)
        SELECT s.specialty_code                 AS specialty_code,
               s.specialty_name                 AS specialty_name,
               COUNT(DISTINCT s.specialist_mbn) AS specialist_count
        FROM specialists s
                 LEFT JOIN units u ON u.name = s.unit_name
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
        WHERE (:unitName::VARCHAR IS NULL OR u.name = :unitName::VARCHAR)
          AND (:brigadeName::VARCHAR IS NULL OR b.name = :brigadeName::VARCHAR)
          AND (:corpsName::VARCHAR IS NULL OR c.name = :corpsName::VARCHAR)
          AND (:divisionName::VARCHAR IS NULL OR d.name = :divisionName::VARCHAR)
          AND (:armyName::VARCHAR IS NULL OR a.name = :armyName::VARCHAR)
        GROUP BY s.specialty_code, s.specialty_name
        HAVING (COUNT(DISTINCT s.specialist_mbn) >= :minSpecialistCount::INTEGER OR :minSpecialistCount::INTEGER IS NULL)
           AND (COUNT(DISTINCT s.specialist_mbn) <= :maxSpecialistCount::INTEGER OR :maxSpecialistCount::INTEGER IS NULL)
        ORDER BY specialist_count DESC;',
        '{unitName, brigadeName, corpsName, divisionName, armyName, minSpecialistCount, maxSpecialistCount}')
ON CONFLICT DO NOTHING;