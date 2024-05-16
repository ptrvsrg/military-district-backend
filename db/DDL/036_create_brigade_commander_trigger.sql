CREATE OR REPLACE TRIGGER check_brigade_commander
    BEFORE INSERT OR UPDATE
    ON brigades
    FOR EACH ROW
EXECUTE FUNCTION check_commander();