package com.alejandro.leadboardbackend.exception;

public class UserExistsException extends BusinessException {

    public UserExistsException(String message) {
        super(message);
    }

    public UserExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
