package com.alejandro.leadboardbackend.service.impl;

import com.alejandro.leadboardbackend.domain.entity.ProjectImage;
import com.alejandro.leadboardbackend.exception.business.ResourceNotFoundException;
import com.alejandro.leadboardbackend.exception.fileException.FileUploadException;
import com.alejandro.leadboardbackend.repository.ProjectImageRepository;
import com.alejandro.leadboardbackend.service.ProjectImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProjectImageServiceImpl implements ProjectImageService {

    private final CloudinaryServiceImpl cloudinaryServiceImpl;
    private final ProjectImageRepository projectImageRepository;

    @Override
    @Transactional
    public void deleteGalleryImage(Long projectId, Long imageId) {

        ProjectImage image = projectImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada con id: " + imageId));

        if (!image.getProject().getId().equals(projectId)) {
            throw new IllegalArgumentException("La imagen no pertenece al proyecto indicado");
        }

        try {
            cloudinaryServiceImpl.delete(image.getPublicId());
        } catch (IOException e) {
            throw new FileUploadException("Error al eliminar la imagen de galeria", e);
        }
    }
}
