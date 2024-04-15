WITH attributes AS (SELECT 'Дата получения звания "' || r.name || '"' AS attribute_name,
                           r.id                                       AS rank_id,
                           r.level                                    AS rank_level
                    FROM ranks r),
     rank_militaries AS (SELECT m.id         AS military_id,
                                r.level      AS rank_level,
                                m.birth_date AS birth_date
                         FROM militaries m
                                  JOIN ranks r ON r.id = m.rank_id
                         WHERE m.rank_id IS NOT NULL)
INSERT
INTO military_attributes (military_id, name, rank_id, value)
SELECT rm.military_id                                      AS military_id,
       a.attribute_name                                    AS name,
       a.rank_id                                           AS rank_id,
       date(GREATEST(CURRENT_DATE - ((rm.rank_level - a.rank_level) || ' years')::INTERVAL,
                     rm.birth_date + INTERVAL '18 years')) AS value
FROM rank_militaries rm
         JOIN attributes a ON rm.rank_level >= a.rank_level
ON CONFLICT DO NOTHING;
