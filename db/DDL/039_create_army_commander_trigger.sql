CREATE OR REPLACE TRIGGER check_army_commander
    BEFORE INSERT OR UPDATE
    ON armies
    FOR EACH ROW
EXECUTE FUNCTION check_commander();