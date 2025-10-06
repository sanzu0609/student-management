package com.example.studentmanagement.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApiErrorResponse(
    String code,
    String message,
    List<FieldValidationError> errors
) {

    public ApiErrorResponse {
        errors = errors == null ? List.of() : List.copyOf(errors);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record FieldValidationError(
        String field,
        String message,
        Object rejectedValue
    ) {
    }
}
