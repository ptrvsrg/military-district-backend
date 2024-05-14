INSERT INTO reports(name, description, query, parameters)
VALUES ('Сооружения по количеству дислоцированных подразделений',
        'Получить перечень сооружений указанной военной части, перечень сооружений, где дислоцировано более одного подразделения, где недислоцировано ни одного подразделения',
        'WITH units_with_squad_count AS (SELECT u.name AS unit_name, COUNT(DISTINCT s.id) AS squad_count
                                        FROM units u
                                                 LEFT JOIN companies c ON c.unit_id = u.id
                                                 LEFT JOIN platoons p ON p.company_id = c.id
                                                 LEFT JOIN squads s ON s.platoon_id = p.id
                                        GROUP BY u.name)
        SELECT b.name         AS building_name,
               b.coordinate   AS coordinate,
               b.address      AS address,
               uwsc.unit_name AS unit_name
        FROM buildings b
                 LEFT JOIN units_with_squad_count uwsc ON uwsc.unit_name = b.unit_name
        WHERE (b.unit_name = :unitName::VARCHAR OR :unitName::VARCHAR IS NULL)
          AND (uwsc.squad_count >= :minSquadCount::INTEGER OR :minSquadCount::INTEGER IS NULL)
          AND (uwsc.squad_count <= :maxSquadCount::INTEGER OR :maxSquadCount::INTEGER IS NULL)',
        '{unitName, minSquadCount, maxSquadCount}')
ON CONFLICT DO NOTHING