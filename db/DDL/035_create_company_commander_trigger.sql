CREATE OR REPLACE FUNCTION check_commander() RETURNS TRIGGER AS
$check_commander$
DECLARE
    military_rank_category_name VARCHAR(255);
BEGIN
    SELECT rc.name
    INTO military_rank_category_name
    FROM militaries m
             JOIN ranks r ON r.id = m.rank_id
             JOIN rank_categories rc ON rc.id = r.rank_category_id
    WHERE m.mbn = new.commander_mbn;

    IF military_rank_category_name != 'Офицерский состав' THEN
        RAISE EXCEPTION 'Military rank category must be "Офицерский состав"';
    END IF;

    RETURN new;
END;
$check_commander$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER check_company_commander
    BEFORE INSERT OR UPDATE
    ON companies
    FOR EACH ROW
EXECUTE FUNCTION check_commander();