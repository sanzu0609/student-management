package com.example.studentmanagement.exception;

public class InvalidStudentDataException extends RuntimeException {

    public InvalidStudentDataException(String message) {
        super(message);
    }
}