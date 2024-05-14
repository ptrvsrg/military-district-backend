package ru.nsu.ccfit.petrov.database.military_district.report.service;

import static ru.nsu.ccfit.petrov.database.military_district.report.util.SpecPageSortUtils.generatePageable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.report.dto.ReportBuildInputDto;
import ru.nsu.ccfit.petrov.database.military_district.report.dto.ReportBuildOutputDto;
import ru.nsu.ccfit.petrov.database.military_district.report.dto.ReportInfoOutputDto;
import ru.nsu.ccfit.petrov.database.military_district.report.exception.ReportNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.report.mapper.ReportMapper;
import ru.nsu.ccfit.petrov.database.military_district.report.persistence.repository.ReportRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReportService {

  private final ReportRepository reportRepository;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final ReportMapper reportMapper;

  @Cacheable(value = "reports", key = "#a0 + '_' + #a1", sync = true)
  public List<ReportInfoOutputDto> getReports(Integer page, Integer pageSize) {
    log.info("Get reports: page={}, pageSize={}", page, pageSize);
    var sort = Sort.by("name").ascending();
    var pageable = generatePageable(page, pageSize, sort);
    return reportRepository.findAll(pageable).getContent().stream()
        .map(reportMapper::toOutputDto)
        .toList();
  }

  @Cacheable(value = "report_count", sync = true)
  public long getReportCount() {
    log.info("Get report count");
    return reportRepository.count();
  }

  @Cacheable(value = "report", key = "#a0", sync = true)
  public ReportInfoOutputDto getReport(@NonNull String name) {
    log.info("Get report: name={}", name);
    var report = reportRepository.findByName(name).orElseThrow(ReportNotFoundException::new);
    return reportMapper.toOutputDto(report);
  }

  @Cacheable(value = "build_report", key = "#a0 + '_' + #a1", sync = true)
  public ReportBuildOutputDto buildReport(
      @NonNull String name, @NonNull ReportBuildInputDto inputDto) {
    log.info("Build report: input={}", inputDto);

    var report = reportRepository.findByName(name).orElseThrow(ReportNotFoundException::new);

    var paramSource = new MapSqlParameterSource();
    report.getParameters().forEach(param -> paramSource.addValue(param, null));
    inputDto.getParameters().forEach(paramSource::addValue);

    var result = namedParameterJdbcTemplate.queryForList(report.getQuery(), paramSource);
    var data = result.stream().map(this::processRow).toList();

    return ReportBuildOutputDto.builder().data(data).build();
  }

  private Map<String, String> processRow(Map<String, Object> row) {
    return row.entrySet().stream()
        .collect(
            Collectors.toMap(
                Map.Entry::getKey,
                entry -> {
                  var value = entry.getValue();
                  return value == null ? "—" : value.toString();
                }));
  }
}
