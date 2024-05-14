package ru.nsu.ccfit.petrov.database.military_district.report.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.ccfit.petrov.database.military_district.report.persistence.entity.Report;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Integer> {
  Optional<Report> findByName(String name);

  long deleteByName(String name);

  boolean existsByName(String name);
}
