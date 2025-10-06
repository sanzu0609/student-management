package com.example.studentmanagement.exception;

public class InvalidStudentDataException extends RuntimeException {

    private final String field;
    private final Object rejectedValue;

    public InvalidStudentDataException(String field, Object rejectedValue, String message) {
        super(message);
        this.field = field;
        this.rejectedValue = rejectedValue;
    }

    public String getField() {
        return field;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }
}
