INSERT INTO reports (name, description, query, parameters)
VALUES ('Офицеры по воинскому званию и формированиям',
        'Получить данные по офицерскому составу в целом и по офицерскому составу указанного звания всех частей военного округа, отдельной армии, дивизии, корпуса, военной части',
        'WITH officers AS (SELECT m.mbn       AS officer_mbn,
                                  CONCAT(m.last_name,
                                         '' '',
                                         m.first_name,
                                         CASE WHEN m.middle_name IS NULL
                                                  THEN ''''
                                                  ELSE CONCAT('' '', m.middle_name)
                                         END
                                  )           AS officer_name,
                                  r.name      AS rank,
                                  m.unit_name AS unit_name
                          FROM militaries m
                                   JOIN ranks r            ON r.id = m.rank_id
                                   JOIN rank_categories rc ON rc.id = r.rank_category_id
                          WHERE rc.name = ''Офицерский состав'')
        SELECT DISTINCT o.officer_mbn                                                                         AS officer_mbn,
                        o.officer_name                                                                        AS officer_name,
                        o.rank                                                                                AS rank,
                        u.name                                                                                AS unit_name,
                        NULLIF(ARRAY_TO_STRING(ARRAY_REMOVE(ARRAY_AGG(DISTINCT b.name), NULL), '', ''), '''') AS brigade_names,
                        NULLIF(ARRAY_TO_STRING(ARRAY_REMOVE(ARRAY_AGG(DISTINCT c.name), NULL), '', ''), '''') AS corps_names,
                        NULLIF(ARRAY_TO_STRING(ARRAY_REMOVE(ARRAY_AGG(DISTINCT d.name), NULL), '', ''), '''') AS division_names,
                        NULLIF(ARRAY_TO_STRING(ARRAY_REMOVE(ARRAY_AGG(DISTINCT a.name), NULL), '', ''), '''') AS army_names
        FROM officers o
                 LEFT JOIN units u             ON u.name = o.unit_name
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
        WHERE (:rankName::VARCHAR IS NULL     OR o.rank = :rankName::VARCHAR)
          AND (:unitName::VARCHAR IS NULL     OR u.name = :unitName::VARCHAR)
          AND (:brigadeName::VARCHAR IS NULL  OR b.name = :brigadeName::VARCHAR)
          AND (:corpsName::VARCHAR IS NULL    OR c.name = :corpsName::VARCHAR)
          AND (:divisionName::VARCHAR IS NULL OR d.name = :divisionName::VARCHAR)
          AND (:armyName::VARCHAR IS NULL     OR a.name = :armyName::VARCHAR)
        GROUP BY o.officer_mbn, o.officer_name, o.rank, u.name
        ORDER BY officer_mbn',
        ARRAY [
            ('rankName', 'SELECT name FROM ranks')::REPORT_PARAMETER,
            ('unitName', 'SELECT name FROM units')::REPORT_PARAMETER,
            ('brigadeName', 'SELECT name FROM brigades')::REPORT_PARAMETER,
            ('corpsName', 'SELECT name FROM corps')::REPORT_PARAMETER,
            ('divisionName', 'SELECT name FROM divisions')::REPORT_PARAMETER,
            ('armyName', 'SELECT name FROM armies')::REPORT_PARAMETER])
ON CONFLICT DO NOTHING;