package com.example.studentmanagement.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(
    Instant timestamp,
    int status,
    String error,
    String message,
    String path,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<FieldValidationError> errors
) {

    public ApiErrorResponse {
        timestamp = timestamp == null ? Instant.now() : timestamp;
        errors = errors == null ? List.of() : List.copyOf(errors);
    }

    public record FieldValidationError(
        String field,
        String message,
        Object rejectedValue
    ) {
    }
}
