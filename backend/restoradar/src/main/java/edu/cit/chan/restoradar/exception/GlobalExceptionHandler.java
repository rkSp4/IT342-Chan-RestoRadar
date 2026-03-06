package edu.cit.chan.restoradar.exception;

import edu.cit.chan.restoradar.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ErrorResponse.ErrorDetail detail = new ErrorResponse.ErrorDetail("VALID-001", "Validation failed", errors);
        ErrorResponse response = new ErrorResponse(false, null, detail, Instant.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(DuplicateEmailException ex) {
        ErrorResponse.ErrorDetail detail = new ErrorResponse.ErrorDetail("DB-002", ex.getMessage(), null);
        ErrorResponse response = new ErrorResponse(false, null, detail, Instant.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        ErrorResponse.ErrorDetail detail = new ErrorResponse.ErrorDetail("AUTH-001", ex.getMessage(), null);
        ErrorResponse response = new ErrorResponse(false, null, detail, Instant.now());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<ErrorResponse> handleTokenRefresh(TokenRefreshException ex) {
        ErrorResponse.ErrorDetail detail = new ErrorResponse.ErrorDetail("AUTH-004", ex.getMessage(), null);
        ErrorResponse response = new ErrorResponse(false, null, detail, Instant.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse.ErrorDetail detail = new ErrorResponse.ErrorDetail("SYSTEM-001", "An unexpected error occurred", ex.getMessage());
        ErrorResponse response = new ErrorResponse(false, null, detail, Instant.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}