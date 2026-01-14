package com.alejandro.leadboardbackend.exception.fileException;

public class InvalidFileException extends FileException {

    public InvalidFileException(String message) {
        super(message);
    }

    public InvalidFileException(String fileType, String allowedTypes) {
        super(String.format("Tipo de archivo inválido: '%s'. Permitidos: %s", fileType, allowedTypes));
    }
}
