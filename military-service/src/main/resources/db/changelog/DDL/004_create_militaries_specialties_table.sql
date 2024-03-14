CREATE TABLE IF NOT EXISTS military_service.militaries_specialties
(
    military_id  INTEGER NOT NULL REFERENCES military_service.militaries (id) ON DELETE CASCADE,
    specialty_id INTEGER NOT NULL REFERENCES military_service.specialties (id) ON DELETE CASCADE,
    PRIMARY KEY (military_id, specialty_id)
);

COMMENT ON TABLE military_service.militaries_specialties IS 'Военно-учётные специальности военнослужащих';

COMMENT ON COLUMN military_service.militaries_specialties.military_id IS 'ID военнослужащего';
COMMENT ON COLUMN military_service.militaries_specialties.specialty_id IS 'ID военно-учётной специальности';