CREATE TABLE IF NOT EXISTS buildings_companies
(
    building_id  INTEGER      NOT NULL REFERENCES buildings (id) ON DELETE CASCADE ON UPDATE CASCADE,
    company_name VARCHAR(255) NOT NULL REFERENCES companies (name) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (building_id, company_name)
);

COMMENT ON TABLE buildings_companies IS 'Сооружения рот';

COMMENT ON COLUMN buildings_companies.building_id IS 'ID сооружения';
COMMENT ON COLUMN buildings_companies.company_name IS 'Наименование роты';