package com.alejandro.leadboardbackend.service;

import com.alejandro.leadboardbackend.dto.ProjectRequestDto;
import com.alejandro.leadboardbackend.dto.ProjectResponseDto;
import com.alejandro.leadboardbackend.exception.FileUploadException;
import com.alejandro.leadboardbackend.exception.InvalidFileException;
import com.alejandro.leadboardbackend.exception.ResourceNotFoundException;
import com.alejandro.leadboardbackend.mapper.ProjectMapper;
import com.alejandro.leadboardbackend.model.Project;
import com.alejandro.leadboardbackend.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProjectService implements  IProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    @Transactional
    public ProjectResponseDto saveProject(ProjectRequestDto requestDto, MultipartFile mainImage, List<MultipartFile> gallery) {

        logger.info("Creando nuevo proyecto: {}", requestDto.getTitle());

        Project project = projectMapper.toEntity(requestDto);

        // Subir imagen principal
        if (mainImage != null && !mainImage.isEmpty()) {
            logger.debug("Subiendo imagen principal");
            project.setMainImageUrl(uploadFile(mainImage, "imagen principal"));
        }

        // Subir galería
        if (gallery != null && !gallery.isEmpty()) {
            logger.debug("Subiendo {} imágenes de galería", gallery.size());
            List<String> galleryUrls = gallery.stream()
                    .map(file -> uploadFile(file, "galería"))
                    .toList(); // Java 16+, si no, usa collect(Collectors.toList())
            project.setGalleryUrls(galleryUrls);
        }

        Project savedProject = projectRepository.save(project);
        logger.info("Proyecto creado exitosamente con id: {}", savedProject.getId());

        return projectMapper.toResponseDto(savedProject);
    }

    // -------------------------
    // Helper
    // -------------------------

    private String uploadFile(MultipartFile file, String context) {
        try {
            Map<String, Object> result = cloudinaryService.upload(file);
            return (String) result.get("url");
        } catch (InvalidFileException e) {
            throw new InvalidFileException(context + ": " + e.getMessage());
        } catch (Exception e) {
            throw new FileUploadException("Error al subir archivo de " + context, e);
        }
    }
}
