package com.customizationaudit.shared.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Clock clock;

    public GlobalExceptionHandler(Clock clock) {
        this.clock = clock;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(new ApiErrorResponse(
                Instant.now(clock),
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                exception.getMessage(),
                List.of(),
                null
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<ApiErrorResponse.ApiErrorDetail> details = exception.getBindingResult().getFieldErrors().stream()
                .map(this::toDetail)
                .toList();
        return ResponseEntity.badRequest().body(new ApiErrorResponse(
                Instant.now(clock),
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                "Request validation failed.",
                details,
                null
        ));
    }

    private ApiErrorResponse.ApiErrorDetail toDetail(FieldError fieldError) {
        return new ApiErrorResponse.ApiErrorDetail(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
