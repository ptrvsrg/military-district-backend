INSERT INTO rank_categories (name, created_at, updated_at)
SELECT rcn.name AS name,
       NOW() AS created_at,
       NOW() AS updated_at
FROM (VALUES ('Рядовой и сержантский состав'),
             ('Офицерский состав')) AS rcn(name)
ON CONFLICT DO NOTHING;