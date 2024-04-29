package ru.nsu.ccfit.petrov.database.military_district.report.persistence.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
  public boolean existsByName(String name) {
    var routineName = REPORT_GEN_PREFIX + name;
    var sql = "SELECT EXISTS (SELECT 1 FROM pg_catalog.pg_proc pp WHERE pp.proname = ?)";
    return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, routineName));
  }

  @Override
  public List<ReportProjection> findAll() {
    var sql =
        "WITH pronames_with_args AS ("
            + "SELECT pp.proname AS name, UNNEST(pp.proargnames) AS arg_name, UNNEST(pp.proargmodes) AS arg_mode "
            + "FROM pg_proc pp WHERE pp.proname LIKE ?) "
            + "SELECT pwa.name AS name, STRING_AGG(pwa.arg_name, ',') AS parameters "
            + "FROM pronames_with_args pwa "
            + "WHERE pwa.arg_mode = 'i' "
            + "GROUP BY pwa.name "
            + "ORDER BY pwa.name";
    return jdbcTemplate.query(
        sql, (rs, rowNum) -> mapResultSetToProjection(rs), REPORT_GEN_PREFIX + "%");
  }

  @Override
  public List<ReportProjection> findAll(int page, int pageSize) {
    var sql =
        "WITH pronames_with_args AS ("
            + "SELECT proname AS name, UNNEST(proargnames) AS arg_name, UNNEST(proargmodes) AS arg_mode "
            + "FROM pg_proc WHERE proname LIKE ?) "
            + "SELECT pwa.name AS name, STRING_AGG(pwa.arg_name, ',') AS parameters "
            + "FROM pronames_with_args pwa "
            + "WHERE arg_mode = 'i' "
            + "GROUP BY pwa.name "
            + "ORDER BY pwa.name "
            + "OFFSET ? "
            + "LIMIT ?";
    return jdbcTemplate.query(
        sql,
        (rs, rowNum) -> mapResultSetToProjection(rs),
        REPORT_GEN_PREFIX + "%",
        page * pageSize,
        pageSize);
  }

  @Override
  public Optional<ReportProjection> findByName(String name) {
    if (!existsByName(name)) {
      return Optional.empty();
    }

    var args = findArgumentsByName(name);
    var parameters =
        args.stream().filter(a -> a.getMode().equals("i")).map(Argument::getName).toList();
    var columns =
        args.stream().filter(a -> a.getMode().equals("t")).map(Argument::getName).toList();
    var report =
        ReportProjection.builder().name(name).columns(columns).parameters(parameters).build();
    return Optional.of(report);
  }

  @Override
  public Optional<Report> findWithDataByName(String name, List<Parameter> parameters) {
    if (!existsByName(name)) {
      return Optional.empty();
    }

    var parametersMap =
        parameters.stream().collect(Collectors.toMap(Parameter::getName, Parameter::getValue));

    var args = findArgumentsByName(name);
    var parameterNames =
        args.stream().filter(a -> a.getMode().equals("i")).map(Argument::getName).toList();
    var types = args.stream().filter(a -> a.getMode().equals("i")).map(Argument::getType).toList();
    var columns =
        args.stream().filter(a -> a.getMode().equals("t")).map(Argument::getName).toList();
    var parameterValues = parameterNames.stream().map(parametersMap::get).toList();

    var data = executeRoutine(REPORT_GEN_PREFIX + name, parameterValues, types);

    return Optional.of(
        Report.builder().name(name).columns(columns).parameters(parameterNames).data(data).build());
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
    var parameters = rs.getString("parameters");
    if (parameters != null) {
      List<String> parameterList = Arrays.asList(parameters.split(","));
      projection.setParameters(parameterList);
    }
    return projection;
  }

  private List<Argument> findArgumentsByName(String name) {
    var routineName = REPORT_GEN_PREFIX + name;
    var sql =
        "SELECT a.arg_name AS name, FORMAT_TYPE(a.arg_type, NULL) AS type, a.arg_mode AS mode "
            + "FROM pg_proc pp "
            + "JOIN pg_type pt ON pt.oid = pp.prorettype "
            + "LEFT JOIN UNNEST(pp.proargnames, pp.proargtypes, pp.proargmodes) WITH ORDINALITY AS a(arg_name, arg_type, arg_mode, arg_order) ON a.arg_order >= 0 "
            + "WHERE pp.proname = ?";
    return jdbcTemplate.query(sql, (rs, colNum) -> mapResultSetToArgument(rs), routineName);
  }

  private Argument mapResultSetToArgument(ResultSet rs) throws SQLException {
    return Argument.builder()
        .name(rs.getString("name"))
        .type(rs.getString("type"))
        .mode(rs.getString("mode"))
        .build();
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  private static class Argument {
    private String name;
    private String type;
    private String mode;
  }
}
