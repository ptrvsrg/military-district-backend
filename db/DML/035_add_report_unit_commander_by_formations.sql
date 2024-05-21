INSERT INTO reports(name, description, query, parameters)
VALUES ('Воинские части и их командиры по формированиям',
        'Получить перечень всех частей военного округа, указанной армии, дивизии, корпуса и их командиров',
        'WITH units_with_commander AS (SELECT u.name                                         AS unit_name,
                                              u.commander_mbn                                AS commander_mbn,
                                              ARRAY_REMOVE(ARRAY_AGG(DISTINCT b.name), NULL) AS brigade_names,
                                              ARRAY_REMOVE(ARRAY_AGG(DISTINCT c.name), NULL) AS corps_names,
                                              ARRAY_REMOVE(ARRAY_AGG(DISTINCT d.name), NULL) AS division_names,
                                              ARRAY_REMOVE(ARRAY_AGG(DISTINCT a.name), NULL) AS army_names
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
                                      WHERE (:brigadeName::VARCHAR IS NULL  OR b.name = :brigadeName::VARCHAR)
                                        AND (:corpsName::VARCHAR IS NULL    OR c.name = :corpsName::VARCHAR)
                                        AND (:divisionName::VARCHAR IS NULL OR d.name = :divisionName::VARCHAR)
                                        AND (:armyName::VARCHAR IS NULL     OR a.name = :armyName::VARCHAR)
                                      GROUP BY u.name, u.commander_mbn)
        SELECT uwc.unit_name                                             AS unit_name,
               m.mbn                                                     AS commander_mbn,
               CONCAT(m.last_name,
                      '' '',
                      m.first_name,
                      CASE WHEN m.middle_name IS NULL
                               THEN ''''
                               ELSE CONCAT('' '', m.middle_name)
                      END
               )                                                         AS commander_name,
               NULLIF(ARRAY_TO_STRING(uwc.brigade_names, '', ''), '''')  AS brigade_names,
               NULLIF(ARRAY_TO_STRING(uwc.corps_names, '', ''), '''')    AS corps_names,
               NULLIF(ARRAY_TO_STRING(uwc.division_names, '', ''), '''') AS division_names,
               NULLIF(ARRAY_TO_STRING(uwc.army_names, '', ''), '''')     AS army_names
        FROM units_with_commander uwc
                 JOIN militaries m ON m.mbn = uwc.commander_mbn
        ORDER BY uwc.unit_name',
        ARRAY [
            ('brigadeName', 'SELECT name FROM brigades')::REPORT_PARAMETER,
            ('corpsName', 'SELECT name FROM corps')::REPORT_PARAMETER,
            ('divisionName', 'SELECT name FROM divisions')::REPORT_PARAMETER,
            ('armyName', 'SELECT name FROM armies')::REPORT_PARAMETER])
ON CONFLICT DO NOTHING;