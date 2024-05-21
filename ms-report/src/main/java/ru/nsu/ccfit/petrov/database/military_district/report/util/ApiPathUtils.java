package ru.nsu.ccfit.petrov.database.military_district.report.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiPathUtils {

  public static final String REPORT_PREFIX = "/reports";
  public static final String BUILD_SUFFIX = "/build";
  public static final String EXPORT_SUFFIX = "/export";
  public static final String GET_REPORT_SUFFIX = "/one";
  public static final String GET_ALL_REPORTS_SUFFIX = "/all";
  public static final String GET_REPORT_COUNT_SUFFIX = "/count";
  public static final String GET_REPORT_PARAMETER_VALUES_SUFFIX = "/parameter-values";
}
