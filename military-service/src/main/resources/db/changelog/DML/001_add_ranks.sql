WITH rank_category_id AS (SELECT c.id AS id
                          FROM military_service.rank_categories c
                          WHERE name = 'Рядовой и сержантский состав'),
     rank_names_levels AS (SELECT rnl.name  AS name,
                                  rnl.level AS level
                           FROM (VALUES ('Рядовой', 1),
                                        ('Ефрейтор', 2),
                                        ('Младший сержант', 3),
                                        ('Сержант', 4),
                                        ('Старший сержант', 5),
                                        ('Старшина', 6),
                                        ('Прапорщик', 7),
                                        ('Старший прапорщик', 8)) AS rnl(name, level))
INSERT
INTO military_service.ranks (name, level, rank_category_id, created_at, updated_at)
SELECT rnl.name  AS name,
       rnl.level AS level,
       rci.id    AS rank_category_id,
       NOW()     AS created_at,
       NOW()     AS updated_at
FROM rank_category_id rci,
     rank_names_levels rnl
ON CONFLICT DO NOTHING;

WITH rank_category_id AS (SELECT c.id AS id
                          FROM military_service.rank_categories c
                          WHERE name = 'Офицерский состав'),
     rank_names_levels AS (SELECT rnl.name  AS name,
                                  rnl.level AS level
                           FROM (VALUES ('Младший лейтенант', 9),
                                        ('Лейтенант', 10),
                                        ('Старший лейтенант', 11),
                                        ('Капитан', 12),
                                        ('Майор', 13),
                                        ('Подполковник', 14),
                                        ('Полковник', 15),
                                        ('Генерал-майор', 16),
                                        ('Генерал-лейтенант', 17),
                                        ('Генерал-полковник', 18),
                                        ('Генерал армии', 19),
                                        ('Маршал Российской Федерации', 20)) AS rnl(name, level))
INSERT
INTO military_service.ranks (name, level, rank_category_id, created_at, updated_at)
SELECT rnl.name  AS name,
       rnl.level AS level,
       rci.id    AS rank_category_id,
       NOW()     AS created_at,
       NOW()     AS updated_at
FROM rank_category_id rci,
     rank_names_levels rnl
ON CONFLICT DO NOTHING;