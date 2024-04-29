package ru.nsu.ccfit.petrov.database.military_district.report.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.servlet.support.RequestContextUtils;
import ru.nsu.ccfit.petrov.database.military_district.report.dto.ErrorDto;
import ru.nsu.ccfit.petrov.database.military_district.report.exception.ReportNotFoundException;

@RestControllerAdvice
@RequiredArgsConstructor
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
    return ResponseEntity.status(NOT_FOUND)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(getMessage("exception.endpoint-not-found", request))
                .build());
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorDto> handleHttpRequestMethodNotSupportedException(
      HttpServletRequest request) {
    return ResponseEntity.status(METHOD_NOT_ALLOWED)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(getMessage("exception.method-not-supported", request))
                .build());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorDto> handleHttpMessageNotReadableException(
      HttpServletRequest request) {
    return ResponseEntity.status(BAD_REQUEST)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(getMessage("exception.invalid-json-format", request))
                .build());
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorDto> handleMissingServletRequestParameterException(
      HttpServletRequest request) {
    return ResponseEntity.status(BAD_REQUEST)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(getMessage("exception.missing-request-parameter", request))
                .build());
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ErrorDto> handleHttpMediaTypeNotSupportedException(
      HttpServletRequest request) {
    return ResponseEntity.status(UNSUPPORTED_MEDIA_TYPE)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(getMessage("exception.unsupported-media-type", request))
                .build());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorDto> handleAccessDeniedException(HttpServletRequest request) {
    return ResponseEntity.status(FORBIDDEN)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(getMessage("exception.access-denied", request))
                .build());
  }

  @ExceptionHandler(ReportNotFoundException.class)
  public ResponseEntity<ErrorDto> handleReportNotFoundException(HttpServletRequest request) {
    return ResponseEntity.status(NOT_FOUND)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(getMessage("exception.report-not-found", request))
                .build());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> handleException(HttpServletRequest request) {
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
