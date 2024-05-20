package ru.nsu.ccfit.petrov.database.military_district.report.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.servlet.support.RequestContextUtils;
import ru.nsu.ccfit.petrov.database.military_district.report.dto.ErrorDto;
import ru.nsu.ccfit.petrov.database.military_district.report.exception.DuplicateReportException;
import ru.nsu.ccfit.petrov.database.military_district.report.exception.ReportNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.report.exception.ReportParameterNotFoundException;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ExceptionCatcher {

  private final MessageSource messageSource;

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(
      HttpServletRequest request, MethodArgumentNotValidException e) {
    var errors =
        e.getBindingResult().getFieldErrors().stream()
            .collect(
                Collectors.groupingBy(
                    FieldError::getField,
                    Collectors.mapping(FieldError::getDefaultMessage, Collectors.joining(" "))));
    log.warn("handleMethodArgumentNotValidException: {}", errors);
    return ResponseEntity.status(BAD_REQUEST)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(getMessage("exception.validation-error", request))
                .errors(errors)
                .build());
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorDto> handleNoHandlerFoundException(HttpServletRequest request) {
    var message = getMessage("exception.endpoint-not-found", request);
    log.warn("handleNoHandlerFoundException: {}", message);
    return ResponseEntity.status(NOT_FOUND)
        .body(ErrorDto.builder().createdAt(Instant.now()).message(message).build());
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorDto> handleNoResourceFoundException(HttpServletRequest request) {
    var message = getMessage("exception.endpoint-not-found", request);
    log.warn("handleNoResourceFoundException: {}", message);
    return ResponseEntity.status(NOT_FOUND)
        .body(ErrorDto.builder().createdAt(Instant.now()).message(message).build());
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorDto> handleHttpRequestMethodNotSupportedException(
      HttpServletRequest request) {
    var message = getMessage("exception.method-not-supported", request);
    log.warn("handleHttpRequestMethodNotSupportedException: {}", message);
    return ResponseEntity.status(METHOD_NOT_ALLOWED)
        .body(ErrorDto.builder().createdAt(Instant.now()).message(message).build());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorDto> handleHttpMessageNotReadableException(
      HttpServletRequest request) {
    var message = getMessage("exception.invalid-json-format", request);
    log.warn("handleHttpMessageNotReadableException: {}", message);
    return ResponseEntity.status(BAD_REQUEST)
        .body(ErrorDto.builder().createdAt(Instant.now()).message(message).build());
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorDto> handleMissingServletRequestParameterException(
      HttpServletRequest request) {
    var message = getMessage("exception.missing-request-parameter", request);
    log.warn("handleMissingServletRequestParameterException: {}", message);
    return ResponseEntity.status(BAD_REQUEST)
        .body(ErrorDto.builder().createdAt(Instant.now()).message(message).build());
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ErrorDto> handleHttpMediaTypeNotSupportedException(
      HttpServletRequest request) {
    var message = getMessage("exception.unsupported-media-type", request);
    log.warn("handleHttpMediaTypeNotSupportedException: {}", message);
    return ResponseEntity.status(UNSUPPORTED_MEDIA_TYPE)
        .body(ErrorDto.builder().createdAt(Instant.now()).message(message).build());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorDto> handleAccessDeniedException(HttpServletRequest request) {
    var message = getMessage("exception.access-denied", request);
    log.warn("handleAccessDeniedException: {}", message);
    return ResponseEntity.status(FORBIDDEN)
        .body(ErrorDto.builder().createdAt(Instant.now()).message(message).build());
  }

  @ExceptionHandler(ReportParameterNotFoundException.class)
  public ResponseEntity<ErrorDto> handleReportParameterNotFoundException(HttpServletRequest request) {
    var message = getMessage("exception.report_parameter-not-found", request);
    log.warn("handleReportParameterNotFoundException: {}", message);
    return ResponseEntity.status(NOT_FOUND)
            .body(ErrorDto.builder().createdAt(Instant.now()).message(message).build());
  }

  @ExceptionHandler(ReportNotFoundException.class)
  public ResponseEntity<ErrorDto> handleReportNotFoundException(HttpServletRequest request) {
    var message = getMessage("exception.report-not-found", request);
    log.warn("handleReportNotFoundException: {}", message);
    return ResponseEntity.status(NOT_FOUND)
            .body(ErrorDto.builder().createdAt(Instant.now()).message(message).build());
  }

  @ExceptionHandler(DuplicateReportException.class)
  public ResponseEntity<ErrorDto> handleDuplicateReportException(HttpServletRequest request) {
    var message = getMessage("exception.duplicate-report", request);
    log.warn("handleDuplicateReportException: {}", message);
    return ResponseEntity.status(CONFLICT)
        .body(ErrorDto.builder().createdAt(Instant.now()).message(message).build());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> handleException(HttpServletRequest request, Exception e) {
    log.error("handleException", e);
    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(getMessage("exception.internal-server-error", request))
                .build());
  }

  private String getMessage(String code, HttpServletRequest request) {
    return messageSource.getMessage(code, null, RequestContextUtils.getLocale(request));
  }
}
