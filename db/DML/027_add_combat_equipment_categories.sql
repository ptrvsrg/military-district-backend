INSERT INTO combat_equipment_categories (name)
VALUES ('Танки'),
       ('БТР'),
       ('БМП')
ON CONFLICT DO NOTHING;