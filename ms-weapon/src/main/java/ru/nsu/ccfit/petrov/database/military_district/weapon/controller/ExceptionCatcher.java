package ru.nsu.ccfit.petrov.database.military_district.weapon.controller;

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
import ru.nsu.ccfit.petrov.database.military_district.weapon.exception.WeaponAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.weapon.exception.WeaponCategoryAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.weapon.exception.WeaponCategoryNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.weapon.exception.WeaponNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.weapon.exception.WeaponTypeAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.weapon.exception.WeaponTypeNotFoundException;

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
    if (e instanceof WeaponAlreadyExistsException) {
      return handleWeaponAlreadyExistsException((WeaponAlreadyExistsException) e, env);
    }
    if (e instanceof WeaponNotFoundException) {
      return handleWeaponNotFoundException((WeaponNotFoundException) e, env);
    }
    if (e instanceof WeaponTypeNotFoundException) {
      return handleWeaponTypeNotFoundException((WeaponTypeNotFoundException) e, env);
    }
    if (e instanceof WeaponTypeAlreadyExistsException) {
      return handleWeaponTypeAlreadyExistsException((WeaponTypeAlreadyExistsException) e, env);
    }
    if (e instanceof WeaponCategoryNotFoundException) {
      return handleWeaponCategoryNotFoundException((WeaponCategoryNotFoundException) e, env);
    }
    if (e instanceof WeaponCategoryAlreadyExistsException) {
      return handleWeaponCategoryAlreadyExistsException(
          (WeaponCategoryAlreadyExistsException) e, env);
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
    return GraphQLError.newError().errorType(BAD_REQUEST).message(errors).build();
  }

  private GraphQLError handleWeaponAlreadyExistsException(
      WeaponAlreadyExistsException e, DataFetchingEnvironment env) {
    return GraphQLError.newError()
        .errorType(BAD_REQUEST)
        .message(getMessage("exception.weapon.already-exists", env))
        .build();
  }

  private GraphQLError handleWeaponNotFoundException(
      WeaponNotFoundException e, DataFetchingEnvironment env) {
    return GraphQLError.newError()
        .errorType(NOT_FOUND)
        .message(getMessage("exception.weapon.not-found", env))
        .build();
  }

  private GraphQLError handleWeaponTypeNotFoundException(
      WeaponTypeNotFoundException e, DataFetchingEnvironment env) {
    return GraphQLError.newError()
        .errorType(NOT_FOUND)
        .message(getMessage("exception.weapon-type.not-found", env))
        .build();
  }

  private GraphQLError handleWeaponTypeAlreadyExistsException(
      WeaponTypeAlreadyExistsException e, DataFetchingEnvironment env) {
    return GraphQLError.newError()
        .errorType(BAD_REQUEST)
        .message(getMessage("exception.weapon-type.already-exists", env))
        .build();
  }

  private GraphQLError handleWeaponCategoryNotFoundException(
      WeaponCategoryNotFoundException e, DataFetchingEnvironment env) {
    return GraphQLError.newError()
        .errorType(NOT_FOUND)
        .message(getMessage("exception.weapon-category.not-found", env))
        .build();
  }

  private GraphQLError handleWeaponCategoryAlreadyExistsException(
      WeaponCategoryAlreadyExistsException e, DataFetchingEnvironment env) {
    return GraphQLError.newError()
        .errorType(BAD_REQUEST)
        .message(getMessage("exception.weapon-category.already-exists", env))
        .build();
  }

  private GraphQLError handleDataIntegrityViolationException(
      DataIntegrityViolationException e, DataFetchingEnvironment env) {
    if (!(e.getCause() instanceof org.hibernate.exception.ConstraintViolationException)) {
      return handleException(e, env);
    }

    log.error(e.getMessage());
    var sql = ((org.hibernate.exception.ConstraintViolationException) e.getCause()).getSQL();
    return GraphQLError.newError()
        .errorType(getErrorTypeBySql(sql))
        .message(getMessage(getMessageCodeBySql(sql), env))
        .build();
  }

  private GraphQLError handleAccessDeniedException(
      AccessDeniedException e, DataFetchingEnvironment env) {
    return GraphQLError.newError()
        .errorType(FORBIDDEN)
        .message(getMessage("exception.access-denied", env))
        .build();
  }

  private GraphQLError handleException(Throwable e, DataFetchingEnvironment env) {
    log.error(e.getMessage(), e);
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
