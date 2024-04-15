CREATE OR REPLACE TRIGGER check_unit_commander
    BEFORE INSERT OR UPDATE
    ON units
    FOR EACH ROW
EXECUTE FUNCTION check_commander();