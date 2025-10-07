package com.example.studentmanagement.exception;

public class InvalidStudentDataException extends RuntimeException {

    private final String field;
    private final Object rejectedValue;
    private final String messageKey;
    private final Object[] messageArgs;

    public InvalidStudentDataException(String field, Object rejectedValue, String messageKey, Object... messageArgs) {
        super(messageKey);
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.messageKey = messageKey;
        this.messageArgs = messageArgs == null ? new Object[0] : messageArgs.clone();
    }

    public String getField() {
        return field;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object[] getMessageArgs() {
        return messageArgs.clone();
    }
}
