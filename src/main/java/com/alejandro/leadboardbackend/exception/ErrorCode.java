package com.alejandro.leadboardbackend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // ---------- GENERALES ----------
    INTERNAL_ERROR("GEN_001", HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor"),
    VALIDATION_ERROR("GEN_002", HttpStatus.BAD_REQUEST, "Datos inválidos"),
    INVALID_ARGUMENT("GEN_400", HttpStatus.BAD_REQUEST, "Argumento inválido"),

    // ---------- PROYECTOS ----------
    PROJECT_NOT_FOUND("PROJECT_404", HttpStatus.NOT_FOUND, "Proyecto no encontrado"),
    PROJECT_BUSINESS_ERROR("PROJECT_400", HttpStatus.BAD_REQUEST, "Error de negocio en proyecto"),

    // ---------- ARCHIVOS ----------
    FILE_TOO_LARGE("FILE_001", HttpStatus.PAYLOAD_TOO_LARGE, "Archivo demasiado grande"),
    FILE_UPLOAD_ERROR("FILE_002", HttpStatus.INTERNAL_SERVER_ERROR, "Error al subir archivo"),
    INVALID_FILE("FILE_003", HttpStatus.BAD_REQUEST, "Archivo inválido");

    private final String code;
    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(String code, HttpStatus status, String defaultMessage) {
        this.code = code;
        this.status = status;
        this.defaultMessage = defaultMessage;
    }
}
