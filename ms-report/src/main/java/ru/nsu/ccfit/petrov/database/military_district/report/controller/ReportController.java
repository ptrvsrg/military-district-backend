package ru.nsu.ccfit.petrov.database.military_district.report.controller;

import static org.springframework.http.HttpStatus.OK;
import static ru.nsu.ccfit.petrov.database.military_district.report.util.ApiPathUtils.BUILD_SUFFIX;
import static ru.nsu.ccfit.petrov.database.military_district.report.util.ApiPathUtils.EXPORT_SUFFIX;
import static ru.nsu.ccfit.petrov.database.military_district.report.util.ApiPathUtils.GET_ALL_REPORTS_SUFFIX;
import static ru.nsu.ccfit.petrov.database.military_district.report.util.ApiPathUtils.GET_REPORT_COUNT_SUFFIX;
import static ru.nsu.ccfit.petrov.database.military_district.report.util.ApiPathUtils.GET_REPORT_PARAMETER_VALUES_SUFFIX;
import static ru.nsu.ccfit.petrov.database.military_district.report.util.ApiPathUtils.GET_REPORT_SUFFIX;
import static ru.nsu.ccfit.petrov.database.military_district.report.util.ApiPathUtils.REPORT_PREFIX;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.ccfit.petrov.database.military_district.report.dto.ReportBuildInputDto;
import ru.nsu.ccfit.petrov.database.military_district.report.dto.ReportBuildOutputDto;
import ru.nsu.ccfit.petrov.database.military_district.report.dto.ReportInfoOutputDto;
import ru.nsu.ccfit.petrov.database.military_district.report.service.CsvService;
import ru.nsu.ccfit.petrov.database.military_district.report.service.ReportService;

@RestController
@RequiredArgsConstructor
@RequestMapping(REPORT_PREFIX)
public class ReportController {

  private final ReportService reportService;
  private final CsvService csvService;

  @GetMapping(GET_ALL_REPORTS_SUFFIX)
  @PreAuthorize("hasAuthority('VIEW_REPORTS')")
  public ResponseEntity<List<ReportInfoOutputDto>> getReports(
      @RequestParam(name = "page", required = false) Integer page,
      @RequestParam(name = "pageSize", required = false) Integer pageSize) {
    return ResponseEntity.status(OK).body(reportService.getReports(page, pageSize));
  }

  @GetMapping(GET_REPORT_COUNT_SUFFIX)
  @PreAuthorize("hasAuthority('VIEW_REPORTS')")
  public ResponseEntity<Map<String, Long>> getReportCount() {
    return ResponseEntity.status(OK).body(Map.of("count", reportService.getReportCount()));
  }

  @GetMapping(GET_REPORT_SUFFIX)
  @PreAuthorize("hasAuthority('VIEW_REPORTS')")
  public ResponseEntity<ReportInfoOutputDto> getReport(@RequestParam("report") String report) {
    return ResponseEntity.status(OK).body(reportService.getReport(report));
  }

  @GetMapping(GET_REPORT_PARAMETER_VALUES_SUFFIX)
  @PreAuthorize("hasAuthority('VIEW_REPORTS')")
  public ResponseEntity<?> getReportParameterValues(
      @RequestParam("report") String report,
      @RequestParam(name = "parameter", required = false) String parameter) {
    if (parameter == null) {
      return ResponseEntity.status(OK).body(reportService.getAllParameterValues(report));
    }
    return ResponseEntity.status(OK).body(reportService.getParameterValues(report, parameter));
  }

  @PostMapping(BUILD_SUFFIX)
  @PreAuthorize("hasAuthority('BUILD_REPORT')")
  public ResponseEntity<ReportBuildOutputDto> buildReport(
      @RequestParam("report") String report, @RequestBody ReportBuildInputDto inputDto) {
    return ResponseEntity.status(OK).body(reportService.buildReport(report, inputDto));
  }

  @PostMapping(value = EXPORT_SUFFIX, produces = "text/csv")
  @PreAuthorize("hasAuthority('BUILD_REPORT')")
  public void exportReport(
      @RequestParam("report") String report,
      @RequestBody ReportBuildInputDto inputDto,
      HttpServletResponse response)
      throws IOException {
    var outputDto = reportService.buildReport(report, inputDto);
    var csvStream = csvService.convertToCSV(outputDto.getData());

    response.setStatus(OK.value());
    IOUtils.copyLarge(csvStream, response.getOutputStream());
    IOUtils.closeQuietly(csvStream);
  }
}
