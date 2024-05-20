package ru.nsu.ccfit.petrov.database.military_district.report.service;

import static ru.nsu.ccfit.petrov.database.military_district.report.util.SpecPageSortUtils.generatePageable;

import java.util.LinkedHashMap;
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
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.report.dto.ReportBuildInputDto;
import ru.nsu.ccfit.petrov.database.military_district.report.dto.ReportBuildOutputDto;
import ru.nsu.ccfit.petrov.database.military_district.report.dto.ReportInfoOutputDto;
import ru.nsu.ccfit.petrov.database.military_district.report.exception.ReportNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.report.exception.ReportParameterNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.report.mapper.ReportMapper;
import ru.nsu.ccfit.petrov.database.military_district.report.persistence.entity.ReportParameter;
import ru.nsu.ccfit.petrov.database.military_district.report.persistence.repository.ReportRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReportService {

  private final ReportRepository reportRepository;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final ReportMapper reportMapper;

  @Cacheable(value = "reports", key = "#a0 + '_' + #a1", unless = "#result.size() > 1000")
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
  public ReportInfoOutputDto getReport(@NonNull String report) {
    log.info("Get report: report={}", report);
    var reportEntity =
        reportRepository.findByName(report).orElseThrow(ReportNotFoundException::new);
    return reportMapper.toOutputDto(reportEntity);
  }

  @Cacheable(value = "all_report_parameter_values", key = "#a0", sync = true)
  public @Nullable Map<String, List<String>> getAllParameterValues(@NonNull String report) {
    log.info("Get all parameter values: report={}", report);

    var reportEntity =
        reportRepository.findByName(report).orElseThrow(ReportNotFoundException::new);
    if (reportEntity.getParameters().isEmpty()) {
      return null;
    }

    return reportEntity.getParameters().stream()
        .collect(
            Collectors.toMap(
                ReportParameter::getName,
                p -> {
                  if (p.getQueryForValues() == null) {
                    return List.of();
                  }
                  return namedParameterJdbcTemplate.queryForList(
                      p.getQueryForValues(), Map.of(), String.class);
                },
                (existingValue, newValue) -> existingValue,
                LinkedHashMap::new));
  }

  @Cacheable(value = "report_parameter_values", key = "#a0 + '_' + #a1", sync = true)
  public List<String> getParameterValues(@NonNull String report, @NonNull String parameter) {
    log.info("Get parameter values: report={}, parameter={}", report, parameter);

    var reportEntity =
        reportRepository.findByName(report).orElseThrow(ReportNotFoundException::new);
    var parameterEntity =
        reportEntity.getParameters().stream()
            .filter(p -> p.getName().equals(parameter))
            .findFirst();
    if (parameterEntity.isEmpty()) {
      throw new ReportParameterNotFoundException();
    }

    var query = parameterEntity.get().getQueryForValues();
    if (query == null) {
      return List.of();
    }

    return namedParameterJdbcTemplate.queryForList(query, Map.of(), String.class);
  }

  @Cacheable(value = "build_report", key = "#a0 + '_' + #a1", unless = "#result.data.size() > 1000")
  public ReportBuildOutputDto buildReport(
      @NonNull String report, @NonNull ReportBuildInputDto inputDto) {
    log.info("Build report: input={}", inputDto);

    var reportEntity =
        reportRepository.findByName(report).orElseThrow(ReportNotFoundException::new);

    var paramSource = new MapSqlParameterSource();
    reportEntity.getParameters().forEach(param -> paramSource.addValue(param.getName(), null));
    inputDto.getParameters().forEach(paramSource::addValue);

    var result = namedParameterJdbcTemplate.queryForList(reportEntity.getQuery(), paramSource);
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
                },
                (existingValue, newValue) -> existingValue,
                LinkedHashMap::new));
  }
}
