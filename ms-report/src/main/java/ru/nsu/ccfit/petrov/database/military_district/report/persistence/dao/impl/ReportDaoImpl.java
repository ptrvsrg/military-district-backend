package ru.nsu.ccfit.petrov.database.military_district.report.persistence.dao.impl;

import static ru.nsu.ccfit.petrov.database.military_district.report.util.DBUtil.getNullableResult;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.petrov.database.military_district.report.dto.Parameter;
import ru.nsu.ccfit.petrov.database.military_district.report.persistence.dao.ReportDao;
import ru.nsu.ccfit.petrov.database.military_district.report.persistence.entity.Report;
import ru.nsu.ccfit.petrov.database.military_district.report.persistence.entity.ReportProjection;

@Component
@RequiredArgsConstructor
public class ReportDaoImpl implements ReportDao {

  private static final String REPORT_GEN_PREFIX = "gen_report_";

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<ReportProjection> findAll() {
    var sql =
        "SELECT proname AS name, "
            + "ARRAY(SELECT pp.proargnames[i] "
            + "FROM UNNEST(pp.proargmodes) WITH ORDINALITY AS u(mode, i) WHERE u.mode = 'i') AS parameters, "
            + "ARRAY(SELECT pp.proargnames[i] "
            + "FROM UNNEST(pp.proargmodes) WITH ORDINALITY AS u(mode, i) WHERE u.mode = 't') AS columns "
            + "FROM pg_proc pp "
            + "WHERE pp.proname LIKE ? "
            + "ORDER BY pp.proname";
    return getNullableResult(
        () ->
            jdbcTemplate.query(
                sql, (rs, rowNum) -> mapResultSetToProjection(rs), REPORT_GEN_PREFIX + "%"));
  }

  @Override
  public List<ReportProjection> findAll(int page, int pageSize) {
    var sql =
        "SELECT proname AS name, "
            + "ARRAY(SELECT pp.proargnames[i] "
            + "FROM UNNEST(pp.proargmodes) WITH ORDINALITY AS u(mode, i) WHERE u.mode = 'i') AS parameters, "
            + "ARRAY(SELECT pp.proargnames[i] "
            + "FROM UNNEST(pp.proargmodes) WITH ORDINALITY AS u(mode, i) WHERE u.mode = 't') AS columns "
            + "FROM pg_proc pp "
            + "WHERE pp.proname LIKE ? "
            + "ORDER BY pp.proname "
            + "OFFSET ? "
            + "LIMIT ?";
    return getNullableResult(
        () ->
            jdbcTemplate.query(
                sql,
                (rs, rowNum) -> mapResultSetToProjection(rs),
                REPORT_GEN_PREFIX + "%",
                page * pageSize,
                pageSize));
  }

  @Override
  public long count() {
    var sql = "SELECT COUNT(*) FROM pg_proc pp WHERE pp.proname LIKE ?";
    var result = jdbcTemplate.queryForObject(sql, Long.class, REPORT_GEN_PREFIX + "%");
    return Optional.ofNullable(result).orElse(0L);
  }

  @Override
  public Optional<ReportProjection> findByName(String name) {
    var routineName = REPORT_GEN_PREFIX + name;
    var sql =
        "SELECT proname AS name, "
            + "ARRAY(SELECT pp.proargnames[i] "
            + "FROM UNNEST(pp.proargmodes) WITH ORDINALITY AS u(mode, i) WHERE u.mode = 'i') AS parameters, "
            + "ARRAY(SELECT pp.proargnames[i] "
            + "FROM UNNEST(pp.proargmodes) WITH ORDINALITY AS u(mode, i) WHERE u.mode = 't') AS columns "
            + "FROM pg_proc pp "
            + "WHERE pp.proname = ? "
            + "ORDER BY pp.proname";
    var result =
        getNullableResult(
            () ->
                jdbcTemplate.queryForObject(
                    sql, (rs, rowNum) -> mapResultSetToProjection(rs), routineName));
    return Optional.ofNullable(result);
  }

  @Override
  public Optional<Report> findWithDataByName(String name, List<Parameter> parameters) {
    var reportProjection = findByName(name).orElse(null);
    if (reportProjection == null) {
      return Optional.empty();
    }

    var parametersMap =
        parameters.stream().collect(Collectors.toMap(Parameter::getName, Parameter::getValue));

    var types = findParameterTypesByName(name);
    var parameterValues =
        reportProjection.getParameters().stream().map(parametersMap::get).toList();

    var data = executeRoutine(REPORT_GEN_PREFIX + name, parameterValues, types);

    return Optional.of(
        Report.builder()
            .name(name)
            .columns(reportProjection.getColumns())
            .parameters(reportProjection.getParameters())
            .data(data)
            .build());
  }

  private List<String> findParameterTypesByName(String name) {
    var routineName = REPORT_GEN_PREFIX + name;
    var sql =
            "SELECT FORMAT_TYPE(a.arg_type, NULL) AS type "
                    + "FROM pg_proc pp "
                    + "JOIN pg_type pt ON pt.oid = pp.prorettype "
                    + "LEFT JOIN UNNEST(pp.proargtypes, pp.proargmodes) WITH ORDINALITY AS a(arg_type, arg_mode, arg_order) ON a.arg_order >= 0 "
                    + "WHERE pp.proname = ? "
                    + "AND a.arg_mode = 'i'";
    return jdbcTemplate.queryForList(sql, String.class, routineName);
  }

  private List<Map<String, String>> executeRoutine(
      String routineName, List<String> parameterValues, List<String> parameterTypes) {
    var sqlBuilder = new StringBuilder("SELECT * FROM ");
    sqlBuilder.append(routineName);
    sqlBuilder.append("(");

    for (int i = 0; i < parameterValues.size(); i++) {
      sqlBuilder.append("?");
      if (parameterTypes.get(i) != null) {
        sqlBuilder.append("::");
        sqlBuilder.append(parameterTypes.get(i));
      }
      if (i < parameterValues.size() - 1) {
        sqlBuilder.append(",");
      }
    }

    sqlBuilder.append(")");

    var resultList = jdbcTemplate.queryForList(sqlBuilder.toString(), parameterValues.toArray());
    return resultList.stream()
        .map(
            row ->
                row.entrySet().stream()
                    .collect(
                        Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().toString())))
        .collect(Collectors.toList());
  }

  private ReportProjection mapResultSetToProjection(ResultSet rs) throws SQLException {
    var projection = new ReportProjection();

    projection.setName(rs.getString("name").replace(REPORT_GEN_PREFIX, ""));

    var parameters = rs.getArray("parameters");
    if (parameters != null) {
      List<String> parameterList = Arrays.asList((String[]) parameters.getArray());
      projection.setParameters(parameterList);
    }

    var columns = rs.getArray("columns");
    if (columns != null) {
      List<String> columnList = Arrays.asList((String[]) columns.getArray());
      projection.setParameters(columnList);
    }

    return projection;
  }
}
