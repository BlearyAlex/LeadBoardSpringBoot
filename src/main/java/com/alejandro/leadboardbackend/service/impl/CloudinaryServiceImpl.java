package com.alejandro.leadboardbackend.service.impl;

import com.alejandro.leadboardbackend.exception.fileException.InvalidFileException;
import com.alejandro.leadboardbackend.service.CloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    // Tipos de archivos permitidos
    private static final List<String> ALLOWED_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/webp"
    );

    // Tamaño máximo: 10MB
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    public CloudinaryServiceImpl(
            @Value("${cloudinary.cloud_name}") String cloudName,
            @Value("${cloudinary.api_key}") String apiKey,
            @Value("${cloudinary.api_secret}") String apiSecret) {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        this.cloudinary = new Cloudinary(config);
    }

    /**
     * Sube un archivo a Cloudinary
     *
     * @param multipartFile archivo a subir
     * @return Map con la respuesta de Cloudinary (url, public_id, etc.)
     * @throws IOException              si hay error en la subida
     * @throws IllegalArgumentException si el archivo no es válido
     */
    @Override
    public Map<String, Object> upload(MultipartFile multipartFile) throws IOException {
        validateFile(multipartFile);
        File file = null;
        try {
            file = convertToFile(multipartFile);
            return cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        } catch (InvalidFileException e) {
            throw e;
        } catch (Exception e) {
            throw new FileUploadException("Error al subir archivo a Cloudinary");
        } finally {
            if (file != null && file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * Elimina una imagen de Cloudinary usando su public_id
     *
     * @param publicId el ID público de la imagen en Cloudinary
     * @return Map con el resultado de la operación
     * @throws IOException si hay error en la eliminación
     */
    @Override
    public Map<String, Object> delete(String publicId) throws IOException {
        try {
            return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new FileUploadException("Error al eliminar archivo de Cloudinary", e);
        }
    }

    // -------------------------
    // Validaciones
    // -------------------------

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("El archivo está vacío");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidFileException(
                    "El archivo excede el tamaño máximo permitido de "
                            + (MAX_FILE_SIZE / 1024 / 1024) + "MB"
            );
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new InvalidFileException(
                    "Tipo de archivo no permitido. Tipos aceptados: " + ALLOWED_TYPES
            );
        }
    }

    // -------------------------
    // Utilidad
    // -------------------------

    private File convertToFile(MultipartFile multipartFile) throws IOException {
        // Crear archivo temporal con prefijo y sufijo
        String originalFileName = multipartFile.getOriginalFilename();
        String extension = "";

        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        // Crear archivo temporal en el directorio temporal del sistema
        File file = File.createTempFile("cloudinary-upload-", extension);

        // Escribir los bytes usando try-with-resources
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }

        return file;
    }
}
