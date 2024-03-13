CREATE OR REPLACE FUNCTION get_random_rank_id() RETURNS INTEGER AS
$$
DECLARE
    random_rank_id INTEGER;
BEGIN
    IF RANDOM() < 0.05 THEN
        random_rank_id := NULL;
    ELSE
        SELECT id INTO random_rank_id FROM ranks ORDER BY RANDOM() LIMIT 1;
    END IF;

    RETURN random_rank_id;
END;
$$ LANGUAGE plpgsql;

WITH first_names AS (SELECT *
                     FROM (VALUES ('Михаил'),
                                  ('Александр'),
                                  ('Максим'),
                                  ('Артем'),
                                  ('Степан'),
                                  ('Виталий'),
                                  ('Олег'),
                                  ('Сергей'),
                                  ('Кирилл'),
                                  ('Василий')) AS first_name(first_name)),
     last_names AS (SELECT *
                    FROM (VALUES ('Михайлов'),
                                 ('Александров'),
                                 ('Кузнецов'),
                                 ('Петров'),
                                 ('Степанов'),
                                 ('Иванов'),
                                 ('Смирнов'),
                                 ('Сергеев'),
                                 ('Сидоров'),
                                 ('Васильев')) AS last_name(last_name)),
     middle_names AS (SELECT *
                      FROM (VALUES ('Михайлович'),
                                   ('Александрович'),
                                   ('Максимович'),
                                   ('Артемович'),
                                   ('Степанович'),
                                   ('Витальевич'),
                                   ('Олегович'),
                                   ('Сергеевич'),
                                   ('Кириллович'),
                                   ('Васильевич')) AS middle_name(middle_name))
INSERT
INTO militaries (mbn, first_name, last_name, middle_name, birth_date, avatar_url, rank_id, unit_number, created_at,
                 updated_at)
SELECT 'ВС России Э-' || LPAD((ROW_NUMBER() OVER ())::TEXT, 6, '0')                              AS mbn,
       f.first_name                                                                              AS first_name,
       l.last_name                                                                               AS last_name,
       m.middle_name                                                                             AS middle_name,
       date(CURRENT_DATE - INTERVAL '18 years' - INTERVAL '1 days' * FLOOR(RANDOM() * 365 * 50)) AS birth_date,
       ''                                                                                        AS avatar_url,
       get_random_rank_id()                                                                      AS rank_id,
       (ROW_NUMBER() OVER ()) % 50 + 1                                                           AS unit_number,
       NOW()                                                                                     AS created_at,
       NOW()                                                                                     AS uodated_at
FROM first_names f,
     last_names l,
     middle_names m
ON CONFLICT DO NOTHING;

DROP FUNCTION get_random_rank_id()