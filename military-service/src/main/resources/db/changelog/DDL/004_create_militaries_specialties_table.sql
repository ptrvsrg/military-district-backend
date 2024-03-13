CREATE TABLE IF NOT EXISTS militaries_specialties
(
    military_id  INTEGER NOT NULL REFERENCES militaries (id) ON DELETE CASCADE,
    specialty_id INTEGER NOT NULL REFERENCES specialties (id) ON DELETE CASCADE,
    PRIMARY KEY (military_id, specialty_id)
);

COMMENT ON TABLE militaries_specialties IS 'Военно-учётные специальности военнослужащих';

COMMENT ON COLUMN militaries_specialties.military_id IS 'ID военнослужащего';
COMMENT ON COLUMN militaries_specialties.specialty_id IS 'ID военно-учётной специальности';