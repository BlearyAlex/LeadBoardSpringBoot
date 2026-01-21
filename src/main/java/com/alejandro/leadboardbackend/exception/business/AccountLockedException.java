package com.alejandro.leadboardbackend.exception.business;

public class AccountLockedException extends BusinessException {
    public AccountLockedException(String message) {
        super(message);
    }
}
