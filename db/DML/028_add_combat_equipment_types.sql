WITH tank_category_id AS (SELECT cec.id AS category_id
                          FROM combat_equipment_categories cec
                          WHERE name = 'Танки'),
     tanks AS (SELECT *
               FROM (VALUES ('Т-90'), ('Т-14'), ('2С35 «Коалиция-СВ»')) AS tanks(name),
                    tank_category_id),
     apc_category_id AS (SELECT cec.id AS category_id
                         FROM combat_equipment_categories cec
                         WHERE name = 'БТР'),
     apcs AS (SELECT *
              FROM (VALUES ('БТР-82'), ('БТР-МД «Ракушка»'), ('ПТС-4')) AS apcs(name),
                   apc_category_id),
     ifv_category_id AS (SELECT cec.id AS category_id
                         FROM combat_equipment_categories cec
                         WHERE name = 'БМП'),
     ifvs AS (SELECT *
              FROM (VALUES ('ПТРК «Хризантема»'),
                           ('Боевая машина поддержки танков «Терминатор»'),
                           ('БМП-2')) AS ifvs(name),
                   ifv_category_id),
     combat_equipments AS (SELECT * FROM tanks UNION SELECT * FROM apcs UNION SELECT * FROM ifvs)
INSERT
INTO combat_equipment_types (name, category_id)
SELECT c.name        AS name,
       c.category_id AS category_id
FROM combat_equipments c
ON CONFLICT DO NOTHING;