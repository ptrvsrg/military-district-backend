INSERT INTO weapon_categories (name)
VALUES ('Снайперские винтовки'),
       ('Автоматы'),
       ('Реактивные гранаты')
ON CONFLICT DO NOTHING;