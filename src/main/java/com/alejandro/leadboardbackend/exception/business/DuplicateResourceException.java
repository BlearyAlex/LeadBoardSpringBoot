package com.alejandro.leadboardbackend.exception.business;

public class DuplicateResourceException extends BusinessException {
    public DuplicateResourceException(String resource, String field, Object value) {
        super(String.format("%s duplicado con %s: '%s'", resource, field, value));
    }
}
