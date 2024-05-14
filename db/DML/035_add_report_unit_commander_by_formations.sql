INSERT INTO reports(name, description, query, parameters)
VALUES ('Воинские части и их командиры по формированиям',
        'Получить перечень всех частей военного округа, указанной армии, дивизии, корпуса и их командиров',
        'WITH units_with_commander AS (SELECT DISTINCT u.name AS unit_name,
                                              u.commander_mbn AS commander_mbn
                              FROM units u
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
                              WHERE (:brigadeName::VARCHAR IS NULL OR b.name = :brigadeName::VARCHAR)
                                AND (:corpsName::VARCHAR IS NULL OR c.name = :corpsName::VARCHAR)
                                AND (:divisionName::VARCHAR IS NULL OR d.name = :divisionName::VARCHAR)
                                AND (:armyName::VARCHAR IS NULL OR a.name = :armyName::VARCHAR)) AS commander_mbn,
               CONCAT(
                       m.last_name, '' '',
                       m.first_name,
                       CASE
                           WHEN m.middle_name IS NULL THEN ''''
                           ELSE CONCAT('' '', m.middle_name) END) AS commander_name,
               uwc.unit_name                                    AS unit_name
        FROM units_with_commander uwc
                 JOIN militaries m ON m.mbn = uwc.commander_mbn
        ORDER BY uwc.unit_name',
        '{"brigadeName", "corpsName", "divisionName", "armyName"}')
ON CONFLICT DO NOTHING 