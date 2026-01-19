package com.alejandro.leadboardbackend.service.impl;

import com.alejandro.leadboardbackend.domain.entity.ProjectImage;
import com.alejandro.leadboardbackend.exception.business.ResourceNotFoundException;
import com.alejandro.leadboardbackend.repository.ProjectImageRepository;
import com.alejandro.leadboardbackend.service.ProjectImageService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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

        cloudinaryServiceImpl.delete(image.getPublicId());
        
        projectImageRepository.delete(image);
    }
}
