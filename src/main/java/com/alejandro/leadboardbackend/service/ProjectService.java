package com.alejandro.leadboardbackend.service;

import com.alejandro.leadboardbackend.dto.ProjectRequestDto;
import com.alejandro.leadboardbackend.dto.ProjectResponseDto;
import com.alejandro.leadboardbackend.exception.FileUploadException;
import com.alejandro.leadboardbackend.exception.InvalidFileException;
import com.alejandro.leadboardbackend.exception.ResourceNotFoundException;
import com.alejandro.leadboardbackend.mapper.ProjectMapper;
import com.alejandro.leadboardbackend.model.Project;
import com.alejandro.leadboardbackend.model.ProjectImage;
import com.alejandro.leadboardbackend.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ProjectService implements IProjectService {

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
            try {
                Map<String, Object> result = cloudinaryService.upload(mainImage);
                String url = (String) result.get("url");
                String publicId = (String) result.get("public_id");
                project.setMainImageUrl(url);
                project.setMainImagePublicId(publicId);
            } catch (IOException e) {
                throw new FileUploadException("Error al subir la imagen principal");
            }
        }

        // Subir galería
        if (gallery != null && !gallery.isEmpty()) {
            for (MultipartFile file : gallery) {
                try {
                    Map<String, Object> result = cloudinaryService.upload(file);
                    ProjectImage image = new ProjectImage();
                    image.setUrl((String) result.get("url"));
                    image.setPublicId((String) result.get("public_id"));
                    image.setProject(project);
                    project.getGallery().add(image);
                } catch (IOException e) {
                    throw new FileUploadException("Error al subir imagen de galería", e);
                }
            }
        }

        Project savedProject = projectRepository.save(project);

        logger.info("Proyecto creado exitosamente con id: {}", savedProject.getId());

        return projectMapper.toResponseDto(savedProject);
    }

    @Override
    @Transactional
    public ProjectResponseDto editProject(Long projectId, ProjectRequestDto requestDto, MultipartFile mainImage) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con id: " + projectId));

        project.setTitle(requestDto.getTitle());
        project.setDescription(requestDto.getDescription());
        project.setCategory(requestDto.getCategory());
        project.setLocation(requestDto.getLocation());
        project.setProjectYear(requestDto.getProjectYear());
        project.setClientName(requestDto.getClientName());
        project.setTags(requestDto.getTags());

        if (mainImage != null && !mainImage.isEmpty()) {
            try {
                if (project.getMainImagePublicId() != null) {
                    cloudinaryService.delete(project.getMainImagePublicId());
                }

                // Subir nueva
                Map<String, Object> result = cloudinaryService.upload(mainImage);
                project.setMainImageUrl((String) result.get("url"));
                project.setMainImagePublicId((String) result.get("public_id"));

            } catch (IOException e) {
                throw new FileUploadException("Error al subir imagen principal", e);
            }
        }

        Project updatedProject = projectRepository.save(project);

        return projectMapper.toResponseDto(updatedProject);
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
