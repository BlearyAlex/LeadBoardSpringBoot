package com.alejandro.leadboardbackend.exception.fileException;

public class FileStorageException extends FileException {
    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
