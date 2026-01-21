package com.alejandro.leadboardbackend.exception.business;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
