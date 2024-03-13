WITH attributes AS (SELECT d.id    AS attribute_id,
                           r.level AS rank_level
                    FROM military_attribute_definitions d
                             JOIN ranks r ON r.id = d.rank_id
                    WHERE d.name LIKE 'Дата получения звания "%"'),
     rank_militaries AS (SELECT m.id         AS military_id,
                                r.level      AS rank_level,
                                m.birth_date AS birth_date
                         FROM militaries m
                                  JOIN ranks r ON r.id = m.rank_id
                         WHERE m.rank_id IS NOT NULL)
INSERT
INTO military_attributes (military_id, attribute_definition_id, value)
SELECT rm.military_id                                      AS military_id,
       a.attribute_id                                      AS attribute_definition_id,
       date(GREATEST(CURRENT_DATE - ((rm.rank_level - a.rank_level) || ' years')::INTERVAL,
                     rm.birth_date + INTERVAL '18 years')) AS value
FROM rank_militaries rm
         JOIN attributes a ON rm.rank_level >= a.rank_level
ON CONFLICT DO NOTHING;
