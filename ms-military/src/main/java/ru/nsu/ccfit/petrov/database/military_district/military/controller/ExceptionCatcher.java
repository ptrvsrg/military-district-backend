package ru.nsu.ccfit.petrov.database.military_district.military.controller;

import static org.springframework.graphql.execution.ErrorType.BAD_REQUEST;
import static org.springframework.graphql.execution.ErrorType.FORBIDDEN;
import static org.springframework.graphql.execution.ErrorType.INTERNAL_ERROR;
import static org.springframework.graphql.execution.ErrorType.NOT_FOUND;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.petrov.database.military_district.military.exception.MilitaryAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.military.exception.MilitaryNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.military.exception.RankNotFoundException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExceptionCatcher extends DataFetcherExceptionResolverAdapter {

  private final MessageSource messageSource;

  @Override
  protected GraphQLError resolveToSingleError(
      @NonNull Throwable e, @NonNull DataFetchingEnvironment env) {
    if (e instanceof ConstraintViolationException) {
      return handleConstraintViolationException((ConstraintViolationException) e, env);
    }
    if (e instanceof MilitaryAlreadyExistsException) {
      return handleMilitaryAlreadyExistsException((MilitaryAlreadyExistsException) e, env);
    }
    if (e instanceof MilitaryNotFoundException) {
      return handleMilitaryNotFoundException((MilitaryNotFoundException) e, env);
    }
    if (e instanceof RankNotFoundException) {
      return handleRankNotFoundException((RankNotFoundException) e, env);
    }
    if (e instanceof DataIntegrityViolationException) {
      return handleDataIntegrityViolationException((DataIntegrityViolationException) e, env);
    }
    if (e instanceof AccessDeniedException) {
      return handleAccessDeniedException((AccessDeniedException) e, env);
    }
    return handleException(e, env);
  }

  private GraphQLError handleConstraintViolationException(
      ConstraintViolationException e, DataFetchingEnvironment env) {
    var errors =
        e.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .reduce((m1, m2) -> m1 + " " + m2)
            .orElse("");
    log.warn("handleConstraintViolationException: {}", errors);
    return GraphQLError.newError().errorType(BAD_REQUEST).message(errors).build();
  }

  private GraphQLError handleMilitaryAlreadyExistsException(
      MilitaryAlreadyExistsException e, DataFetchingEnvironment env) {
    var message = getMessage("exception.military-already-exists", env);
    log.warn("handleMilitaryAlreadyExistsException: {}", message);
    return GraphQLError.newError().errorType(BAD_REQUEST).message(message).build();
  }

  private GraphQLError handleMilitaryNotFoundException(
      MilitaryNotFoundException e, DataFetchingEnvironment env) {
    var message = getMessage("exception.military-not-found", env);
    log.warn("handleMilitaryNotFoundException: {}", message);
    return GraphQLError.newError().errorType(NOT_FOUND).message(message).build();
  }

  private GraphQLError handleRankNotFoundException(
      RankNotFoundException e, DataFetchingEnvironment env) {
    var message = getMessage("exception.rank-not-found", env);
    log.warn("handleRankNotFoundException: {}", message);
    return GraphQLError.newError().errorType(NOT_FOUND).message(message).build();
  }

  private GraphQLError handleDataIntegrityViolationException(
      DataIntegrityViolationException e, DataFetchingEnvironment env) {
    if (!(e.getCause() instanceof org.hibernate.exception.ConstraintViolationException)) {
      return handleException(e, env);
    }

    log.error("handleDataIntegrityViolationException", e);
    var sql = ((org.hibernate.exception.ConstraintViolationException) e.getCause()).getSQL();
    return GraphQLError.newError()
        .errorType(getErrorTypeBySql(sql))
        .message(getMessage(getMessageCodeBySql(sql), env))
        .build();
  }

  private GraphQLError handleAccessDeniedException(
      AccessDeniedException e, DataFetchingEnvironment env) {
    log.warn("handleAccessDeniedException: {}", e.getMessage());
    return GraphQLError.newError()
        .errorType(FORBIDDEN)
        .message(getMessage("exception.access-denied", env))
        .build();
  }

  private GraphQLError handleException(Throwable e, DataFetchingEnvironment env) {
    log.error("handleException", e);
    return GraphQLError.newError()
        .errorType(INTERNAL_ERROR)
        .message(getMessage("exception.internal-server-error", env))
        .build();
  }

  private String getMessage(String code, DataFetchingEnvironment env) {
    return messageSource.getMessage(code, null, env.getLocale());
  }

  private String getMessageCodeBySql(String sql) {
    if (sql.toLowerCase().startsWith("insert")) return "exception.data-integrity-violation.insert";
    if (sql.toLowerCase().startsWith("update")) return "exception.data-integrity-violation.update";
    if (sql.toLowerCase().startsWith("delete")) return "exception.data-integrity-violation.delete";
    if (sql.toLowerCase().startsWith("select")) return "exception.data-integrity-violation.select";
    return "exception.internal-server-error";
  }

  private ErrorType getErrorTypeBySql(String sql) {
    if (StringUtils.startsWithAny(sql.toLowerCase(), "insert", "update", "delete", "select")) {
      return BAD_REQUEST;
    }
    return INTERNAL_ERROR;
  }
}
