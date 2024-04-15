ALTER TABLE IF EXISTS militaries
    ADD FOREIGN KEY (unit_name) REFERENCES units (name);