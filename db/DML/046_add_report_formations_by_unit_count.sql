INSERT INTO reports(name, description, query, parameters)
VALUES ('Формирования по количеству военных частей',
        'Получить данные об армии, дивизии, корпусе, в которые входит больше всего (меньше всего) военных частей',
        'WITH army_rating_by_unit_count AS (SELECT DISTINCT a.name               AS army_name,
                                                           COUNT(DISTINCT u.id) AS unit_count
                                           FROM armies a
                                                    LEFT JOIN armies_brigades ab ON ab.army_id = a.id
                                                    LEFT JOIN armies_corps ac ON ac.army_id = a.id
                                                    LEFT JOIN armies_divisions ad ON ad.army_id = a.id
                                                    LEFT JOIN brigades_units bu ON bu.brigade_id = ab.brigade_id
                                                    LEFT JOIN corps_units cu ON cu.corps_id = ac.corps_id
                                                    LEFT JOIN divisions_units du ON du.division_id = ad.division_id
                                                    LEFT JOIN units u ON u.id = bu.unit_id OR u.id = cu.unit_id OR u.id = du.unit_id
                                           GROUP BY a.name),
             brigade_rating_by_unit_count AS (SELECT b.name      AS brigade_name,
                                                     COUNT(u.id) AS unit_count
                                              FROM brigades b
                                                       LEFT JOIN brigades_units bu ON bu.brigade_id = b.id
                                                       LEFT JOIN units u ON u.id = bu.unit_id
                                              GROUP BY b.name),
             corps_rating_by_unit_count AS (SELECT DISTINCT c.name      AS corps_name,
                                                            COUNT(u.id) AS unit_count
                                            FROM corps c
                                                     LEFT JOIN corps_units cu ON cu.corps_id = c.id
                                                     LEFT JOIN units u ON u.id = cu.unit_id
                                            GROUP BY c.name),
             division_rating_by_unit_count AS (SELECT d.name      AS division_name,
                                                      COUNT(u.id) AS unit_count
                                               FROM divisions d
                                                        LEFT JOIN divisions_units du ON du.division_id = d.id
                                                        LEFT JOIN units u ON u.id = du.unit_id
                                               GROUP BY d.name),
             formation_rating_by_unit_count AS (SELECT aruc.army_name  AS formation_name,
                                                       ''Армия''       AS formation_type,
                                                       aruc.unit_count AS unit_count
                                                FROM army_rating_by_unit_count aruc
                                                UNION ALL
                                                SELECT bruc.brigade_name AS formation_name,
                                                       ''Бригада''       AS formation_type,
                                                       bruc.unit_count   AS unit_count
                                                FROM brigade_rating_by_unit_count bruc
                                                UNION ALL
                                                SELECT cruc.corps_name AS formation_name,
                                                       ''Корпус''      AS formation_type,
                                                       cruc.unit_count AS unit_count
                                                FROM corps_rating_by_unit_count cruc
                                                UNION ALL
                                                SELECT druc.division_name AS formation_name,
                                                       ''Дивизия''        AS formation_type,
                                                       druc.unit_count    AS unit_count
                                                FROM division_rating_by_unit_count druc)
        SELECT *
        FROM formation_rating_by_unit_count fruc
        ORDER BY fruc.unit_count DESC, fruc.formation_type;',
        '{}')
ON CONFLICT DO NOTHING;