INSERT INTO rank_categories (name)
VALUES ('Рядовой и сержантский состав'),
       ('Офицерский состав')
ON CONFLICT DO NOTHING;