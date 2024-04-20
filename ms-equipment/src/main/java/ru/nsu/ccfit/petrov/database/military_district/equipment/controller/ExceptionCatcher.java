package ru.nsu.ccfit.petrov.database.military_district.equipment.controller;

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
import ru.nsu.ccfit.petrov.database.military_district.equipment.exception.CombatEquipmentAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.equipment.exception.CombatEquipmentCategoryAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.equipment.exception.CombatEquipmentCategoryNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.equipment.exception.CombatEquipmentNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.equipment.exception.CombatEquipmentTypeAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.equipment.exception.CombatEquipmentTypeNotFoundException;

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
    if (e instanceof CombatEquipmentAlreadyExistsException) {
      return handleCombatEquipmentAlreadyExistsException(
          (CombatEquipmentAlreadyExistsException) e, env);
    }
    if (e instanceof CombatEquipmentNotFoundException) {
      return handleCombatEquipmentNotFoundException((CombatEquipmentNotFoundException) e, env);
    }
    if (e instanceof CombatEquipmentTypeAlreadyExistsException) {
      return handleCombatEquipmentTypeAlreadyExistsException(
          (CombatEquipmentTypeAlreadyExistsException) e, env);
    }
    if (e instanceof CombatEquipmentTypeNotFoundException) {
      return handleCombatEquipmentTypeNotFoundException(
          (CombatEquipmentTypeNotFoundException) e, env);
    }
    if (e instanceof CombatEquipmentCategoryAlreadyExistsException) {
      return handleCombatEquipmentCategoryAlreadyExistsException(
          (CombatEquipmentCategoryAlreadyExistsException) e, env);
    }
    if (e instanceof CombatEquipmentCategoryNotFoundException) {
      return handleCombatEquipmentCategoryNotFoundException(
          (CombatEquipmentCategoryNotFoundException) e, env);
    }
    if (e instanceof AccessDeniedException) {
      return handleAccessDeniedException((AccessDeniedException) e, env);
    }
    if (e instanceof DataIntegrityViolationException) {
      return handleDataIntegrityViolationException((DataIntegrityViolationException) e, env);
    }
    return handleException(e, env);
  }

  private GraphQLError handleConstraintViolationException(
      ConstraintViolationException e, DataFetchingEnvironment env) {
    log.warn("handleConstraintViolationException: ", e);
    var errors =
        e.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .reduce((m1, m2) -> m1 + " " + m2)
            .orElse("");
    return GraphQLError.newError().errorType(BAD_REQUEST).message(errors).build();
  }

  private GraphQLError handleCombatEquipmentAlreadyExistsException(
      CombatEquipmentAlreadyExistsException e, DataFetchingEnvironment env) {
    log.warn("handleCombatEquipmentAlreadyExistsException: ", e);
    return GraphQLError.newError()
        .errorType(BAD_REQUEST)
        .message(getMessage("exception.combat-equipment.already-exists", env))
        .build();
  }

  private GraphQLError handleCombatEquipmentTypeAlreadyExistsException(
      CombatEquipmentTypeAlreadyExistsException e, DataFetchingEnvironment env) {
    log.warn("handleCombatEquipmentTypeAlreadyExistsException: ", e);
    return GraphQLError.newError()
        .errorType(BAD_REQUEST)
        .message(getMessage("exception.combat-equipment-type.already-exists", env))
        .build();
  }

  private GraphQLError handleCombatEquipmentNotFoundException(
      CombatEquipmentNotFoundException e, DataFetchingEnvironment env) {
    log.warn("handleCombatEquipmentNotFoundException: ", e);
    return GraphQLError.newError()
        .errorType(NOT_FOUND)
        .message(getMessage("exception.combat-equipment.not-found", env))
        .build();
  }

  private GraphQLError handleCombatEquipmentTypeNotFoundException(
      CombatEquipmentTypeNotFoundException e, DataFetchingEnvironment env) {
    log.warn("handleCombatEquipmentTypeNotFoundException: ", e);
    return GraphQLError.newError()
        .errorType(NOT_FOUND)
        .message(getMessage("exception.combat-equipment-type.not-found", env))
        .build();
  }

  private GraphQLError handleCombatEquipmentCategoryAlreadyExistsException(
      CombatEquipmentCategoryAlreadyExistsException e, DataFetchingEnvironment env) {
    log.warn("handleCombatEquipmentCategoryAlreadyExistsException: ", e);
    return GraphQLError.newError()
        .errorType(BAD_REQUEST)
        .message(getMessage("exception.combat-equipment-category.already-exists", env))
        .build();
  }

  private GraphQLError handleCombatEquipmentCategoryNotFoundException(
      CombatEquipmentCategoryNotFoundException e, DataFetchingEnvironment env) {
    log.warn("handleCombatEquipmentCategoryNotFoundException: ", e);
    return GraphQLError.newError()
        .errorType(NOT_FOUND)
        .message(getMessage("exception.combat-equipment-category.not-found", env))
        .build();
  }

  private GraphQLError handleDataIntegrityViolationException(
      DataIntegrityViolationException e, DataFetchingEnvironment env) {
    if (!(e.getCause() instanceof org.hibernate.exception.ConstraintViolationException)) {
      return handleException(e, env);
    }

    log.error("handleDataIntegrityViolationException: ", e);
    var sql = ((org.hibernate.exception.ConstraintViolationException) e.getCause()).getSQL();
    return GraphQLError.newError()
        .errorType(getErrorTypeBySql(sql))
        .message(getMessage(getMessageCodeBySql(sql), env))
        .build();
  }

  private GraphQLError handleAccessDeniedException(
      AccessDeniedException e, DataFetchingEnvironment env) {
    log.warn("handleAccessDeniedException: ", e);
    return GraphQLError.newError()
        .errorType(FORBIDDEN)
        .message(getMessage("exception.access-denied", env))
        .build();
  }

  private GraphQLError handleException(Throwable e, DataFetchingEnvironment env) {
    log.error("handleException: ", e);
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
