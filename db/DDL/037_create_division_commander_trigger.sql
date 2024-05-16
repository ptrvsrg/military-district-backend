CREATE OR REPLACE TRIGGER check_division_commander
    BEFORE INSERT OR UPDATE
    ON divisions
    FOR EACH ROW
EXECUTE FUNCTION check_commander();