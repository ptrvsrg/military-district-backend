WITH rank_category_id AS (SELECT c.id AS id
                          FROM rank_categories c
                          WHERE name = 'Рядовой и сержантский состав')
INSERT
INTO ranks (name, level, rank_category_id)
VALUES ('Рядовой', 1, (SELECT id FROM rank_category_id)),
       ('Ефрейтор', 2, (SELECT id FROM rank_category_id)),
       ('Младший сержант', 3, (SELECT id FROM rank_category_id)),
       ('Сержант', 4, (SELECT id FROM rank_category_id)),
       ('Старший сержант', 5, (SELECT id FROM rank_category_id)),
       ('Старшина', 6, (SELECT id FROM rank_category_id)),
       ('Прапорщик', 7, (SELECT id FROM rank_category_id)),
       ('Старший прапорщик', 8, (SELECT id FROM rank_category_id))
ON CONFLICT DO NOTHING;

WITH rank_category_id AS (SELECT c.id
                          FROM rank_categories c
                          WHERE name = 'Офицерский состав')
INSERT
INTO ranks (name, level, rank_category_id)
VALUES ('Младший лейтенант', 9, (SELECT id FROM rank_category_id)),
       ('Лейтенант', 10, (SELECT id FROM rank_category_id)),
       ('Старший лейтенант', 11, (SELECT id FROM rank_category_id)),
       ('Капитан', 12, (SELECT id FROM rank_category_id)),
       ('Майор', 13, (SELECT id FROM rank_category_id)),
       ('Подполковник', 14, (SELECT id FROM rank_category_id)),
       ('Полковник', 15, (SELECT id FROM rank_category_id)),
       ('Генерал-майор', 16, (SELECT id FROM rank_category_id)),
       ('Генерал-лейтенант', 17, (SELECT id FROM rank_category_id)),
       ('Генерал-полковник', 18, (SELECT id FROM rank_category_id)),
       ('Генерал армии', 19, (SELECT id FROM rank_category_id)),
       ('Маршал Российской Федерации', 20, (SELECT id FROM rank_category_id))
ON CONFLICT DO NOTHING;