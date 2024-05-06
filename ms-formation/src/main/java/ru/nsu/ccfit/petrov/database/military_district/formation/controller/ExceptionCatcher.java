package ru.nsu.ccfit.petrov.database.military_district.formation.controller;

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
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.ArmyAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.ArmyNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.BrigadeAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.BrigadeNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.CompanyAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.CompanyNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.CorpsAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.CorpsNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.DivisionAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.DivisionNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.PlatoonAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.PlatoonNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.SquadAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.SquadNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.UnitAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.UnitNotFoundException;

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
    if (e instanceof ArmyAlreadyExistsException) {
      return handleArmyAlreadyExistsException((ArmyAlreadyExistsException) e, env);
    }
    if (e instanceof ArmyNotFoundException) {
      return handleArmyNotFoundException((ArmyNotFoundException) e, env);
    }
    if (e instanceof BrigadeAlreadyExistsException) {
      return handleBrigadeAlreadyExistsException((BrigadeAlreadyExistsException) e, env);
    }
    if (e instanceof BrigadeNotFoundException) {
      return handleBrigadeNotFoundException((BrigadeNotFoundException) e, env);
    }
    if (e instanceof CompanyAlreadyExistsException) {
      return handleCompanyAlreadyExistsException((CompanyAlreadyExistsException) e, env);
    }
    if (e instanceof CompanyNotFoundException) {
      return handleCompanyNotFoundException((CompanyNotFoundException) e, env);
    }
    if (e instanceof CorpsAlreadyExistsException) {
      return handleCorpsAlreadyExistsException((CorpsAlreadyExistsException) e, env);
    }
    if (e instanceof CorpsNotFoundException) {
      return handleCorpsNotFoundException((CorpsNotFoundException) e, env);
    }
    if (e instanceof DivisionAlreadyExistsException) {
      return handleDivisionAlreadyExistsException((DivisionAlreadyExistsException) e, env);
    }
    if (e instanceof DivisionNotFoundException) {
      return handleDivisionNotFoundException((DivisionNotFoundException) e, env);
    }
    if (e instanceof PlatoonAlreadyExistsException) {
      return handlePlatoonAlreadyExistsException((PlatoonAlreadyExistsException) e, env);
    }
    if (e instanceof PlatoonNotFoundException) {
      return handlePlatoonNotFoundException((PlatoonNotFoundException) e, env);
    }
    if (e instanceof SquadAlreadyExistsException) {
      return handleSquadAlreadyExistsException((SquadAlreadyExistsException) e, env);
    }
    if (e instanceof SquadNotFoundException) {
      return handleSquadNotFoundException((SquadNotFoundException) e, env);
    }
    if (e instanceof UnitAlreadyExistsException) {
      return handleUnitAlreadyExistsException((UnitAlreadyExistsException) e, env);
    }
    if (e instanceof UnitNotFoundException) {
      return handleUnitNotFoundException((UnitNotFoundException) e, env);
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

  private GraphQLError handleArmyAlreadyExistsException(
      ArmyAlreadyExistsException e, DataFetchingEnvironment env) {
    var message = getMessage("exception.army-already-exists", env);
    log.warn("handleArmyAlreadyExistsException: {}", message);
    return GraphQLError.newError().errorType(BAD_REQUEST).message(message).build();
  }

  private GraphQLError handleArmyNotFoundException(
      ArmyNotFoundException e, DataFetchingEnvironment env) {
    var message = getMessage("exception.army-not-found", env);
    log.warn("handleArmyNotFoundException: {}", message);
    return GraphQLError.newError().errorType(NOT_FOUND).message(message).build();
  }

  private GraphQLError handleBrigadeAlreadyExistsException(
      BrigadeAlreadyExistsException e, DataFetchingEnvironment env) {
    var message = getMessage("exception.brigade-already-exists", env);
    log.warn("handleBrigadeAlreadyExistsException: {}", message);
    return GraphQLError.newError().errorType(BAD_REQUEST).message(message).build();
  }

  private GraphQLError handleBrigadeNotFoundException(
      BrigadeNotFoundException e, DataFetchingEnvironment env) {
    var message = getMessage("exception.brigade-not-found", env);
    log.warn("handleBrigadeNotFoundException: {}", message);
    return GraphQLError.newError().errorType(NOT_FOUND).message(message).build();
  }

  private GraphQLError handleCompanyAlreadyExistsException(
      CompanyAlreadyExistsException e, DataFetchingEnvironment env) {
    var message = getMessage("exception.company-already-exists", env);
    log.warn("handleCompanyAlreadyExistsException: {}", message);
    return GraphQLError.newError().errorType(BAD_REQUEST).message(message).build();
  }

  private GraphQLError handleCompanyNotFoundException(
      CompanyNotFoundException e, DataFetchingEnvironment env) {
    var message = getMessage("exception.company-not-found", env);
    log.warn("handleCompanyNotFoundException: {}", message);
    return GraphQLError.newError().errorType(NOT_FOUND).message(message).build();
  }

  private GraphQLError handleCorpsAlreadyExistsException(
      CorpsAlreadyExistsException e, DataFetchingEnvironment env) {
    var message = getMessage("exception.corps-already-exists", env);
    log.warn("handleCorpsAlreadyExistsException: {}", message);
    return GraphQLError.newError().errorType(BAD_REQUEST).message(message).build();
  }

  private GraphQLError handleCorpsNotFoundException(
      CorpsNotFoundException e, DataFetchingEnvironment env) {
    var message = getMessage("exception.corps-not-found", env);
    log.warn("handleCorpsNotFoundException: {}", message);
    return GraphQLError.newError().errorType(NOT_FOUND).message(message).build();
  }

  private GraphQLError handleDivisionAlreadyExistsException(
      DivisionAlreadyExistsException e, DataFetchingEnvironment env) {
    var message = getMessage("exception.division-already-exists", env);
    log.warn("handleDivisionAlreadyExistsException: {}", message);
    return GraphQLError.newError().errorType(BAD_REQUEST).message(message).build();
  }

  private GraphQLError handleDivisionNotFoundException(
      DivisionNotFoundException e, DataFetchingEnvironment env) {
    var message = getMessage("exception.division-not-found", env);
    log.warn("handleDivisionNotFoundException: {}", message);
    return GraphQLError.newError().errorType(NOT_FOUND).message(message).build();
  }

  private GraphQLError handlePlatoonAlreadyExistsException(
      PlatoonAlreadyExistsException e, DataFetchingEnvironment env) {
    var message = getMessage("exception.platoon-already-exists", env);
    log.warn("handlePlatoonAlreadyExistsException: {}", message);
    return GraphQLError.newError().errorType(BAD_REQUEST).message(message).build();
  }

  private GraphQLError handlePlatoonNotFoundException(
      PlatoonNotFoundException e, DataFetchingEnvironment env) {
    var message = getMessage("exception.platoon-not-found", env);
    log.warn("handlePlatoonNotFoundException: {}", message);
    return GraphQLError.newError().errorType(NOT_FOUND).message(message).build();
  }

  private GraphQLError handleSquadAlreadyExistsException(
      SquadAlreadyExistsException e, DataFetchingEnvironment env) {
    var message = getMessage("exception.squad-already-exists", env);
    log.warn("handleSquadAlreadyExistsException: {}", message);
    return GraphQLError.newError().errorType(BAD_REQUEST).message(message).build();
  }

  private GraphQLError handleSquadNotFoundException(
      SquadNotFoundException e, DataFetchingEnvironment env) {
    var message = getMessage("exception.squad-not-found", env);
    log.warn("handleSquadNotFoundException: {}", message);
    return GraphQLError.newError().errorType(NOT_FOUND).message(message).build();
  }

  private GraphQLError handleUnitAlreadyExistsException(
      UnitAlreadyExistsException e, DataFetchingEnvironment env) {
    var message = getMessage("exception.unit-already-exists", env);
    log.warn("handleUnitAlreadyExistsException: {}", message);
    return GraphQLError.newError().errorType(BAD_REQUEST).message(message).build();
  }

  private GraphQLError handleUnitNotFoundException(
      UnitNotFoundException e, DataFetchingEnvironment env) {
    var message = getMessage("exception.unit-not-found", env);
    log.warn("handleUnitNotFoundException: {}", message);
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
