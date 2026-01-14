package com.alejandro.leadboardbackend.exception.fileException;

public class FileUploadException extends FileException {

    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
