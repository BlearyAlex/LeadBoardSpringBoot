package com.alejandro.leadboardbackend.service.impl;

import com.alejandro.leadboardbackend.domain.dto.request.ProjectRequestDto;
import com.alejandro.leadboardbackend.domain.dto.response.CloudinaryResponseDto;
import com.alejandro.leadboardbackend.domain.dto.response.ProjectResponseDto;
import com.alejandro.leadboardbackend.exception.fileException.FileUploadException;
import com.alejandro.leadboardbackend.exception.fileException.InvalidFileException;
import com.alejandro.leadboardbackend.exception.business.ResourceNotFoundException;
import com.alejandro.leadboardbackend.mapper.ProjectMapper;
import com.alejandro.leadboardbackend.domain.entity.Project;
import com.alejandro.leadboardbackend.domain.entity.ProjectImage;
import com.alejandro.leadboardbackend.repository.ProjectRepository;
import com.alejandro.leadboardbackend.service.ProjectService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    private final ProjectRepository projectRepository;
    private final  CloudinaryServiceImpl cloudinaryServiceImpl;
    private final ProjectMapper projectMapper;

    @Override
    @Transactional
    public ProjectResponseDto saveProject(ProjectRequestDto requestDto, MultipartFile mainImage, List<MultipartFile> gallery) {

        logger.info("Creando nuevo proyecto: {}", requestDto.getTitle());

        Project project = projectMapper.toEntity(requestDto);

        // Subir imagen principal
        if (mainImage != null && !mainImage.isEmpty()) {
            CloudinaryResponseDto result = cloudinaryServiceImpl.upload(mainImage);
            project.setMainImageUrl(result.getUrl());
            project.setMainImagePublicId(result.getPublicId());
        }

        // Subir galería
        if (gallery != null && !gallery.isEmpty()) {
            for (MultipartFile file : gallery) {
                CloudinaryResponseDto result = cloudinaryServiceImpl.upload(file);
                ProjectImage image = new ProjectImage();
                image.setUrl(result.getUrl());
                image.setPublicId(result.getPublicId());
                image.setProject(project);
                project.getGallery().add(image);
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
            if (project.getMainImagePublicId() != null) {
                cloudinaryServiceImpl.delete(project.getMainImagePublicId());
            }

            // Subir nueva
            CloudinaryResponseDto result = cloudinaryServiceImpl.upload(mainImage);
            project.setMainImageUrl(result.getUrl());
            project.setMainImagePublicId(result.getPublicId());
        }

        Project updatedProject = projectRepository.save(project);

        return projectMapper.toResponseDto(updatedProject);
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->  new ResourceNotFoundException("Proyecto no encontrado con id: " + projectId));

        if (project.getMainImagePublicId() != null) {
            cloudinaryServiceImpl.delete(project.getMainImagePublicId());
        }

        if (project.getGallery() != null) {
            project.getGallery().forEach(image -> {
                cloudinaryServiceImpl.delete(image.getPublicId());
            });
        }
        projectRepository.delete(project);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getAllProjects() {
        return projectMapper.toDtoList(
                projectRepository.findAllWithImages()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponseDto getProjectById(Long projectId) {
        Project project = projectRepository.findByIdWithGallery(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

        return projectMapper.toResponseDto(project);
    }


    // -------------------------
    // Helper
    // -------------------------

    private String uploadFile(MultipartFile file, String context) {
        try {
            CloudinaryResponseDto result = cloudinaryServiceImpl.upload(file);
            return result.getUrl();
        } catch (InvalidFileException e) {
            throw new InvalidFileException(context + ": " + e.getMessage());
        } catch (Exception e) {
            throw new FileUploadException("Error al subir archivo de " + context, e);
        }
    }
}
