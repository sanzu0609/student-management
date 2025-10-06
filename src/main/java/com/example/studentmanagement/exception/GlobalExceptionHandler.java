package com.example.studentmanagement.exception;

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
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<ApiErrorResponse.FieldValidationError> fieldErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(this::toFieldValidationError)
            .toList();

        ApiErrorResponse body = new ApiErrorResponse(
            "VALIDATION_FAILED",
            "Request validation failed.",
            fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(InvalidStudentDataException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidStudentData(InvalidStudentDataException ex) {
        ApiErrorResponse.FieldValidationError error = new ApiErrorResponse.FieldValidationError(
            ex.getField(),
            ex.getMessage(),
            ex.getRejectedValue()
        );

        ApiErrorResponse body = new ApiErrorResponse(
            "INVALID_STUDENT_DATA",
            ex.getMessage(),
            List.of(error)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleStudentNotFound(StudentNotFoundException ex) {
        ApiErrorResponse body = new ApiErrorResponse(
            "STUDENT_NOT_FOUND",
            ex.getMessage(),
            List.of()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
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
