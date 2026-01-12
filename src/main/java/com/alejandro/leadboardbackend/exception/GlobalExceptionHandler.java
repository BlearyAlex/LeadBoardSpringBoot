package com.alejandro.leadboardbackend.exception;

import com.alejandro.leadboardbackend.dto.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ---------------------
    // Método utilitario
    // ---------------------
    private ErrorResponseDto buildError(
            ErrorCode errorCode,
            String message,
            WebRequest request) {

        ErrorResponseDto error = new ErrorResponseDto(
                errorCode.getStatus().value(),
                errorCode.getStatus().getReasonPhrase(),
                message != null ? message : errorCode.getDefaultMessage(),
                request.getContextPath()
        );
        error.setErrorCode(errorCode.getCode());
        return error;
    }

    private String extractPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }

    // ---------------------
    // Handlers generales
    // ---------------------

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponseDto> handleMaxSizeException(MaxUploadSizeExceededException ex, WebRequest request) {
        ErrorResponseDto error = buildError(ErrorCode.FILE_TOO_LARGE, null, request);
        return ResponseEntity.status(ErrorCode.FILE_TOO_LARGE.getStatus()).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        ErrorResponseDto error = buildError(ErrorCode.INVALID_ARGUMENT, ex.getMessage(), request);
        return ResponseEntity.status(ErrorCode.INTERNAL_ERROR.getStatus()).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception ex, WebRequest request) {

        ErrorResponseDto error = buildError(ErrorCode.INTERNAL_ERROR, null, request);

        List<String> details = new ArrayList<>();
        details.add("Ha ocurrido un error interno");
        error.setDetails(details);

        return ResponseEntity.status(ErrorCode.INTERNAL_ERROR.getStatus()).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationErrors(MethodArgumentNotValidException ex, WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        ErrorResponseDto error = buildError(ErrorCode.VALIDATION_ERROR, "Datos invalidos", request);
        error.setDetails(errors);

        return ResponseEntity.badRequest().body(error);
    }

    // ---------------------
    // Handlers de negocio
    // ---------------------

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessException(BusinessException ex, WebRequest request) {
        ErrorResponseDto error = buildError(ErrorCode.PROJECT_BUSINESS_ERROR, ex.getMessage(), request);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponseDto error = buildError(ErrorCode.PROJECT_NOT_FOUND, ex.getMessage(), request);
        return ResponseEntity.status(ErrorCode.PROJECT_NOT_FOUND.getStatus()).body(error);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorResponseDto> handleFileUpload(FileUploadException ex, WebRequest request) {
        ErrorResponseDto error = buildError(ErrorCode.FILE_UPLOAD_ERROR, ex.getMessage(), request);
        return ResponseEntity.status(ErrorCode.FILE_UPLOAD_ERROR.getStatus()).body(error);
    }

    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidFile(InvalidFileException ex, WebRequest request) {
        ErrorResponseDto error = buildError(ErrorCode.INVALID_FILE, ex.getMessage(), request);
        return ResponseEntity.badRequest().body(error);
    }
}
