package com.alejandro.leadboardbackend.exception.business;

public class UserAlreadyExistsException extends BusinessException {

    public UserAlreadyExistsException(String email) {
        super(String.format("El usuario con email '%s' ya existe", email));
    }
}
