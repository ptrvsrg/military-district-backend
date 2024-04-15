INSERT
INTO building_attributes(building_id, name, value)
SELECT b.id                                          AS building_id,
       'Площадь'                                     AS name,
       FLOOR(RANDOM() * (1000 - 300 + 1) + 300)::INT AS value
FROM buildings b
ON CONFLICT DO NOTHING;

INSERT
INTO building_attributes(building_id, name, value)
SELECT b.id                                            AS building_id,
       'Год постройки'                                 AS name,
       FLOOR(RANDOM() * (2024 - 1950 + 1) + 1950)::INT AS value
FROM buildings b
ON CONFLICT DO NOTHING;