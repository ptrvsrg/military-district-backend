WITH sniper_rifle_category_id AS (SELECT cec.id AS category_id
                                  FROM weapon_categories cec
                                  WHERE name = 'Снайперские винтовки'),
     sniper_rifles AS (SELECT *
                       FROM (VALUES ('АСВК'), ('СВД'), ('ВСС «Винторез»')) AS sniper_rifles(name),
                            sniper_rifle_category_id),
     slot_machine_category_id AS (SELECT cec.id AS category_id
                                  FROM weapon_categories cec
                                  WHERE name = 'Автоматы'),
     slot_machines AS (SELECT *
                       FROM (VALUES ('АК-74'), ('АК-15'), ('Вал')) AS slot_machines(name),
                            slot_machine_category_id),
     rocket_grenade_category_id AS (SELECT cec.id AS category_id
                                    FROM weapon_categories cec
                                    WHERE name = 'Реактивные гранаты'),
     rocket_grenades AS (SELECT *
                         FROM (VALUES ('РПГ-7'), ('РПГ-28 «Клюква»'), ('РПГ-30 «Крюк»')) AS rocket_grenades(name),
                              rocket_grenade_category_id),
     weapons AS (SELECT * FROM sniper_rifles UNION SELECT * FROM slot_machines UNION SELECT * FROM rocket_grenades)
INSERT
INTO weapon_types (name, category_id)
SELECT c.name        AS name,
       c.category_id AS category_id
FROM weapons c
ON CONFLICT DO NOTHING;