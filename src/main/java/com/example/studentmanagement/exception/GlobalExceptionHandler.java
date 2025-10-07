package com.example.studentmanagement.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ) {
        List<ApiErrorResponse.FieldValidationError> fieldErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(this::toFieldValidationError)
            .toList();

        ApiErrorResponse body = buildResponse(
            HttpStatus.BAD_REQUEST,
            "Request validation failed.",
            request.getRequestURI(),
            fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(InvalidStudentDataException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidStudentData(
        InvalidStudentDataException ex,
        HttpServletRequest request
    ) {
        ApiErrorResponse.FieldValidationError error = new ApiErrorResponse.FieldValidationError(
            ex.getField(),
            ex.getMessage(),
            ex.getRejectedValue()
        );

        ApiErrorResponse body = buildResponse(
            HttpStatus.BAD_REQUEST,
            ex.getMessage(),
            request.getRequestURI(),
            List.of(error)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(
        ResourceNotFoundException ex,
        HttpServletRequest request
    ) {
        ApiErrorResponse body = buildResponse(
            HttpStatus.NOT_FOUND,
            ex.getMessage(),
            request.getRequestURI(),
            List.of()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    private ApiErrorResponse buildResponse(
        HttpStatus status,
        String message,
        String path,
        List<ApiErrorResponse.FieldValidationError> fieldErrors
    ) {
        return new ApiErrorResponse(
            null,
            status.value(),
            status.getReasonPhrase(),
            message,
            path,
            fieldErrors
        );
    }

    private ApiErrorResponse.FieldValidationError toFieldValidationError(FieldError error) {
        Object rejectedValue = error.getRejectedValue();
        return new ApiErrorResponse.FieldValidationError(
            error.getField(),
            error.getDefaultMessage(),
            rejectedValue
        );
    }
}
