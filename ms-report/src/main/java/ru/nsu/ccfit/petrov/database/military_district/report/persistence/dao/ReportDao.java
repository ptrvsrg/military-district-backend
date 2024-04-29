package ru.nsu.ccfit.petrov.database.military_district.report.persistence.dao;

import java.util.List;
import java.util.Optional;
import ru.nsu.ccfit.petrov.database.military_district.report.dto.Parameter;
import ru.nsu.ccfit.petrov.database.military_district.report.persistence.entity.Report;
import ru.nsu.ccfit.petrov.database.military_district.report.persistence.entity.ReportProjection;

public interface ReportDao {

  List<ReportProjection> findAll();

  List<ReportProjection> findAll(int page, int pageSize);

  long count();

  Optional<ReportProjection> findByName(String name);

  Optional<Report> findWithDataByName(String name, List<Parameter> parameters);
}
