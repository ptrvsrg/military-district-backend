CREATE OR REPLACE TRIGGER check_corps_commander
    BEFORE INSERT OR UPDATE
    ON corps
    FOR EACH ROW
EXECUTE FUNCTION check_commander();