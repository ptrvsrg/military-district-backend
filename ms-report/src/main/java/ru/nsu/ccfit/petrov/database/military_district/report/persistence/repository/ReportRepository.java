package ru.nsu.ccfit.petrov.database.military_district.report.persistence.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.ccfit.petrov.database.military_district.report.persistence.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {
  Optional<Report> findByName(String name);
}
