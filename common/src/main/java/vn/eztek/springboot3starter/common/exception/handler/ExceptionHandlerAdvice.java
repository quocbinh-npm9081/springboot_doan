package vn.eztek.springboot3starter.common.exception.handler;

import jakarta.validation.ConstraintViolationException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;
import vn.eztek.springboot3starter.common.exception.UnauthorizedException;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.ConflictException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.shared.response.ErrorResponse;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Slf4j
@ControllerAdvice
@RestController
@RequiredArgsConstructor
public class ExceptionHandlerAdvice {

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorResponse> handleUsernameAlreadyExistsException(
      ConflictException ex) {
    var errorResponse = ErrorResponse.builder()
        .timestamp(DateUtils.currentZonedDateTime())
        .status(HttpStatus.CONFLICT.value())
        .errorCode(ex.getMessage())
        .build();
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleRoleNotFoundException(
      NotFoundException ex) {
    var errorResponse = ErrorResponse.builder()
        .timestamp(DateUtils.currentZonedDateTime())
        .status(HttpStatus.NOT_FOUND.value())
        .errorCode(ex.getMessage())
        .build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<Object> handle(MethodArgumentNotValidException ex) {
    var error = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);

    if (error == null) {
      var errorResponse = ErrorResponse.builder()
          .timestamp(DateUtils.currentZonedDateTime())
          .status(HttpStatus.BAD_REQUEST.value())
          .errorCode("unknown-error")
          .build();
      return ResponseEntity.badRequest().body(errorResponse);
    }

    var errorResponse = ErrorResponse.builder()
        .timestamp(DateUtils.currentZonedDateTime())
        .status(HttpStatus.BAD_REQUEST.value())
        .errorCode(error.getField() + "-"
            + Objects.requireNonNull(error.getDefaultMessage()).replaceAll("\\s", "-"))
        .build();
    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(
      UsernameNotFoundException ex) {
    var errorResponse = ErrorResponse.builder()
        .timestamp(DateUtils.currentZonedDateTime())
        .status(HttpStatus.UNAUTHORIZED.value())
        .errorCode(ex.getMessage())
        .build();
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentialsException(
      BadCredentialsException ex) {
    var errorResponse = ErrorResponse.builder()
        .timestamp(DateUtils.currentZonedDateTime())
        .status(HttpStatus.UNAUTHORIZED.value())
        .errorCode("username-or-password-is-not-correct")
        .build();
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(
      AccessDeniedException ex) {
    var errorResponse = ErrorResponse.builder()
        .timestamp(DateUtils.currentZonedDateTime())
        .status(HttpStatus.FORBIDDEN.value())
        .errorCode("you-do-not-have-access-to-this-resource")
        .build();
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex) {
    var errorResponse = ErrorResponse.builder()
        .timestamp(DateUtils.currentZonedDateTime())
        .status(HttpStatus.BAD_REQUEST.value())
        .errorCode(ex.getCause().getMessage())
        .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> BadRequestException(
      BadRequestException ex) {
    var errorResponse = ErrorResponse.builder()
        .timestamp(DateUtils.currentZonedDateTime())
        .status(HttpStatus.BAD_REQUEST.value())
        .errorCode(ex.getMessage())
        .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException ex) {
    var errorResponse = ErrorResponse.builder()
        .timestamp(DateUtils.currentZonedDateTime())
        .status(HttpStatus.BAD_REQUEST.value())
        .errorCode(ex.getMessage())
        .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(MultipartException.class)
  public ResponseEntity<ErrorResponse> MultipartException(
      MultipartException ex) {
    var errorResponse = ErrorResponse.builder()
        .timestamp(DateUtils.currentZonedDateTime())
        .status(HttpStatus.BAD_REQUEST.value())
        .errorCode(ex.getMessage())
        .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResponse> MultipartException(
      UnauthorizedException ex) {
    var errorResponse = ErrorResponse.builder()
        .timestamp(DateUtils.currentZonedDateTime())
        .status(HttpStatus.BAD_REQUEST.value())
        .errorCode(ex.getMessage())
        .build();
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }

}
