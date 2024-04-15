CREATE OR REPLACE FUNCTION get_random_unit_name() RETURNS VARCHAR(255) AS
$$
DECLARE
    random_unit_name VARCHAR(255);
BEGIN
    SELECT u.name
    INTO random_unit_name
    FROM units u
    ORDER BY RANDOM()
    LIMIT 1;

    RETURN random_unit_name;
END;
$$ LANGUAGE plpgsql;

UPDATE militaries
SET unit_name = get_random_unit_name()
WHERE TRUE;

DROP FUNCTION get_random_unit_name;
