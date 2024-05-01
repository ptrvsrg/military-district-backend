package ru.nsu.ccfit.petrov.database.military_district.report.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.petrov.database.military_district.report.dto.ReportBuildInputDto;
import ru.nsu.ccfit.petrov.database.military_district.report.dto.ReportBuildOutputDto;
import ru.nsu.ccfit.petrov.database.military_district.report.dto.ReportOutputDto;
import ru.nsu.ccfit.petrov.database.military_district.report.exception.ReportNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.report.mapper.ReportMapper;
import ru.nsu.ccfit.petrov.database.military_district.report.persistence.dao.ReportDao;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

  private final ReportDao reportDao;
  private final ReportMapper reportMapper;

  @Cacheable("reports")
  public List<ReportOutputDto> getReports(Integer page, Integer pageSize) {
    log.info("Get reports: page={}, pageSize={}", page, pageSize);
    if (ObjectUtils.anyNull(page, pageSize)) {
      return reportDao.findAll().stream().map(reportMapper::toOutputDto).toList();
    }
    return reportDao.findAll(page, pageSize).stream().map(reportMapper::toOutputDto).toList();
  }

  @Cacheable("report_count")
  public long getReportCount() {
    log.info("Get report count");
    return reportDao.count();
  }

  @Cacheable("report")
  public ReportOutputDto getReport(String name) {
    log.info("Get report: name={}", name);
    var report = reportDao.findByName(name).orElseThrow(ReportNotFoundException::new);
    return reportMapper.toOutputDto(report);
  }

  @Cacheable("build_report")
  public ReportBuildOutputDto buildReport(ReportBuildInputDto inputDto) {
    log.info("Build report: input={}", inputDto);
    var report =
        reportDao
            .findWithDataByName(inputDto.getName(), inputDto.getParameters())
            .orElseThrow(ReportNotFoundException::new);
    return reportMapper.toBuildOutputDto(report);
  }
}
