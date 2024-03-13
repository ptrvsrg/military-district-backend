package ru.nsu.ccfit.petrov.database.military_district.military.controller;

import static org.springframework.graphql.execution.ErrorType.BAD_REQUEST;
import static org.springframework.graphql.execution.ErrorType.INTERNAL_ERROR;
import static org.springframework.graphql.execution.ErrorType.NOT_FOUND;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.petrov.database.military_district.military.exception.MilitaryAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.military.exception.MilitaryAttributeDefinitionNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.military.exception.MilitaryNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.military.exception.RankNotFoundException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExceptionCatcher extends DataFetcherExceptionResolverAdapter {

  private final MessageSource messageSource;

  @Override
  protected GraphQLError resolveToSingleError(Throwable e, DataFetchingEnvironment env) {
    if (e instanceof ConstraintViolationException)
      return handleConstraintViolationException((ConstraintViolationException) e, env);
    if (e instanceof MilitaryAlreadyExistsException)
      return handleMilitaryAlreadyExistsException((MilitaryAlreadyExistsException) e, env);
    if (e instanceof MilitaryNotFoundException)
      return handleMilitaryNotFoundException((MilitaryNotFoundException) e, env);
    if (e instanceof MilitaryAttributeDefinitionNotFoundException)
      return handleMilitaryAttributeDefinitionNotFoundException((MilitaryAttributeDefinitionNotFoundException) e, env);
    if (e instanceof RankNotFoundException)
      return handleRankNotFoundException((RankNotFoundException) e, env);
    return handleException(e, env);
  }

  private GraphQLError handleConstraintViolationException(
      ConstraintViolationException e, DataFetchingEnvironment env) {
    var errors =
        e.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .reduce((m1, m2) -> m1 + " " + m2)
            .orElse("");
    return GraphQLError.newError()
        .errorType(BAD_REQUEST)
        .message(errors)
        .build();
  }

  private GraphQLError handleMilitaryAlreadyExistsException(
      MilitaryAlreadyExistsException e, DataFetchingEnvironment env) {
    return GraphQLError.newError()
        .errorType(BAD_REQUEST)
        .message(
            messageSource.getMessage("exception.military-already-exists", null, env.getLocale()))
        .build();
  }

  private GraphQLError handleMilitaryNotFoundException(
          MilitaryNotFoundException e, DataFetchingEnvironment env) {
    return GraphQLError.newError()
            .errorType(NOT_FOUND)
            .message(messageSource.getMessage("exception.military-not-found", null, env.getLocale()))
            .build();
  }

  private GraphQLError handleMilitaryAttributeDefinitionNotFoundException(
          MilitaryAttributeDefinitionNotFoundException e, DataFetchingEnvironment env) {
    return GraphQLError.newError()
        .errorType(NOT_FOUND)
        .message(
            messageSource.getMessage(
                "exception.military-attribute-definition-not-found", null, env.getLocale()))
        .build();
  }

  private GraphQLError handleRankNotFoundException(
      RankNotFoundException e, DataFetchingEnvironment env) {
    return GraphQLError.newError()
        .errorType(NOT_FOUND)
        .message(messageSource.getMessage("exception.rank-not-found", null, env.getLocale()))
        .build();
  }

  private GraphQLError handleException(Throwable e, DataFetchingEnvironment env) {
    log.error(e.getMessage(), e);
    return GraphQLError.newError().errorType(INTERNAL_ERROR).message(e.getMessage()).build();
  }
}
