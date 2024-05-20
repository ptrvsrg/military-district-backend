INSERT INTO reports(name, description, query, parameters)
VALUES ('Военнослужащие по специальности и формированиям',
        'Получить перечень военнослужащих указанной специальности в округе, в отдельной армии, дивизии, корпусе, военной части, в указанном подразделении некоторой военной части',
        'WITH specialists AS (SELECT m.mbn         AS specialist_mbn,
                                     m.last_name   AS specialist_last_name,
                                     m.first_name  AS specialist_first_name,
                                     m.middle_name AS specialist_middle_name,
                                     m.unit_name   AS unit_name,
                                     NULLIF(
                                         ARRAY_TO_STRING(
                                             ARRAY_REMOVE(ARRAY_AGG(s.code), NULL),
                                             '', ''
                                         ),
                                         ''''
                                     )             AS specialty_codes
                              FROM militaries m
                                  LEFT JOIN militaries_specialties ms ON ms.military_id = m.id
                                  LEFT JOIN specialties s             ON s.id = ms.specialty_id
                              WHERE (:specialtyCode::VARCHAR IS NULL OR s.code = :specialtyCode::VARCHAR)
                              GROUP BY m.mbn, m.last_name, m.first_name, m.middle_name, m.unit_name)
        SELECT DISTINCT s.specialist_mbn  AS specialist_mbn,
                        CONCAT(s.specialist_last_name,
                               '' '',
                               s.specialist_first_name,
                               CASE WHEN s.specialist_middle_name IS NULL
                                   THEN ''''
                                   ELSE CONCAT('' '', s.specialist_middle_name)
                               END
                        )                 AS specialist_name,
                        s.specialty_codes AS specialty_codes
        FROM specialists s
                 LEFT JOIN units u             ON u.name = s.unit_name
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
        ORDER BY specialist_mbn',
        ARRAY [
            ('specialtyCode', 'SELECT code FROM specialties')::REPORT_PARAMETER,
            ('unitName', 'SELECT name FROM units')::REPORT_PARAMETER,
            ('brigadeName', 'SELECT name FROM brigades')::REPORT_PARAMETER,
            ('corpsName', 'SELECT name FROM corps')::REPORT_PARAMETER,
            ('divisionName', 'SELECT name FROM divisions')::REPORT_PARAMETER,
            ('armyName', 'SELECT name FROM armies')::REPORT_PARAMETER])
ON CONFLICT DO NOTHING;