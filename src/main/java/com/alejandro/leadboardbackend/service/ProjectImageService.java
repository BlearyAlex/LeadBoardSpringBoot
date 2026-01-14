package com.alejandro.leadboardbackend.service;

import com.alejandro.leadboardbackend.exception.fileException.FileUploadException;
import com.alejandro.leadboardbackend.exception.business.ResourceNotFoundException;
import com.alejandro.leadboardbackend.model.ProjectImage;
import com.alejandro.leadboardbackend.repository.ProjectImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ProjectImageService implements IProjectImageService {

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    ProjectImageRepository projectImageRepository;

    @Override
    @Transactional
    public void deleteGalleryImage(Long projectId, Long imageId) {

        ProjectImage image = projectImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada con id: " + imageId));

        if (!image.getProject().getId().equals(projectId)) {
            throw new IllegalArgumentException("La imagen no pertenece al proyecto indicado");
        }

        try {
            cloudinaryService.delete(image.getPublicId());
        } catch (IOException e) {
            throw new FileUploadException("Error al eliminar la imagen de galeria", e);
        }
    }
}
