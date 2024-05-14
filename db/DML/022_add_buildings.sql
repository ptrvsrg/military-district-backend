DO
$$
    BEGIN
        FOR i IN 1..10
            LOOP
                INSERT
                INTO buildings(name, unit_name, coordinate, address)
                SELECT 'Казарма ' || i AS name,
                       u.name          AS unit_name,
                       u.coordinate    AS coordinate,
                       u.address       AS address
                FROM units u
                ON CONFLICT DO NOTHING;
            END LOOP;
    END
$$;

INSERT
INTO buildings(name, unit_name, coordinate, address)
SELECT 'Здание штаба' AS name,
       u.name         AS unit_name,
       u.coordinate   AS coordinate,
       u.address      AS address
FROM units u
ON CONFLICT DO NOTHING;