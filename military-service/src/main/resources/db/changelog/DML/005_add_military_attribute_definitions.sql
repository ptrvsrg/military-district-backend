INSERT
INTO military_attribute_definitions (rank_id, name, description, created_at, updated_at)
SELECT r.id                                           AS rank_id,
       CONCAT('Дата получения звания "', r.name, '"') AS name,
       CONCAT('Дата получения звания "', r.name, '"') AS description,
       NOW()                                          AS created_at,
       NOW()                                          AS updated_at
FROM ranks r
ON CONFLICT DO NOTHING;