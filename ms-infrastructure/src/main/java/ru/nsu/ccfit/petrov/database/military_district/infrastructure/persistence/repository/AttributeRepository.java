package ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.entity.Attribute;

public interface AttributeRepository extends JpaRepository<Attribute, Integer> {}
