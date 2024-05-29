INSERT INTO reports(name, description, query, parameters)
VALUES ('Сооружения по количеству дислоцированных подразделений',
        'Получить перечень сооружений указанной военной части, перечень сооружений, где дислоцировано более одного подразделения, где недислоцировано ни одного подразделения',
        'WITH units_with_squad_count AS (SELECT u.name               AS unit_name,
                                                COUNT(DISTINCT s.id) AS squad_count
                                        FROM units u
                                                 LEFT JOIN companies c ON c.unit_id = u.id
                                                 LEFT JOIN platoons p  ON p.company_id = c.id
                                                 LEFT JOIN squads s    ON s.platoon_id = p.id
                                        GROUP BY u.name)
        SELECT b.name                        AS building_name,
               uwsc.unit_name                AS unit_name,
               b.coordinate                  AS coordinate,
               b.address                     AS address,
               COALESCE(uwsc.squad_count, 0) AS squad_count
        FROM buildings b
                 LEFT JOIN units_with_squad_count uwsc ON uwsc.unit_name = b.unit_name
        WHERE (b.unit_name = :unitName::VARCHAR            OR :unitName::VARCHAR IS NULL)
          AND (COALESCE(uwsc.squad_count, 0) >= :minSquadCount::INTEGER OR :minSquadCount::INTEGER IS NULL)
          AND (COALESCE(uwsc.squad_count, 0) <= :maxSquadCount::INTEGER OR :maxSquadCount::INTEGER IS NULL)',
        ARRAY [
            ('unitName', 'SELECT name FROM units')::REPORT_PARAMETER,
            ('minSquadCount', NULL)::REPORT_PARAMETER,
            ('maxSquadCount', NULL)::REPORT_PARAMETER])
ON CONFLICT DO NOTHING;