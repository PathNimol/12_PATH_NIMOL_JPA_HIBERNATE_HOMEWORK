package org.hrd.hibernatejpa01.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.hrd.hibernatejpa01.model.dto.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** Helper to build error response in unified format **/
    private ResponseEntity<Map<String, Object>> buildErrorResponse(
            HttpStatus status,
            String title,
            String instance,
            Map<String, String> errors
    ) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", "about:blank");
        body.put("title", title);
        body.put("status", status.value());
        body.put("instance", instance);
        body.put("timestamp", LocalDateTime.now());
        if (errors != null && !errors.isEmpty()) {
            body.put("errors", errors);
        }
        return ResponseEntity.status(status).body(body);
    }

    /** BadRequestException **/
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequestException(
            BadRequestException e,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", e.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", request.getRequestURI(), errors);
    }

    /** NotFoundException **/
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(
            NotFoundException e,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", e.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Not Found", request.getRequestURI(), errors);
    }

    /** MaxUploadSizeExceededException **/
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException e,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        errors.put("file", "File size exceeds the maximum limit");
        return buildErrorResponse(HttpStatus.PAYLOAD_TOO_LARGE, "Payload Too Large", request.getRequestURI(), errors);
    }

    /** Validation errors from @Valid DTO fields **/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", request.getRequestURI(), errors);
    }

    /** Validation errors for method parameters **/
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, Object>> handleMethodValidationException(
            HandlerMethodValidationException e,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        e.getParameterValidationResults().forEach(parameterError -> {
            String paramName = parameterError.getMethodParameter().getParameterName();
            parameterError.getResolvableErrors().forEach(messageError -> {
                errors.put(paramName, messageError.getDefaultMessage());
            });
        });
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", request.getRequestURI(), errors);
    }

    /** JSON parse errors **/
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        errors.put("json", "Malformed JSON request");
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", request.getRequestURI(), errors);
    }


    /** Fallback for unhandled exceptions **/
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(
            Exception ex,
            HttpServletRequest request
    ) {
        logger.error("Unhandled exception", ex);
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Something went wrong");
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", request.getRequestURI(), errors);
    }
}
