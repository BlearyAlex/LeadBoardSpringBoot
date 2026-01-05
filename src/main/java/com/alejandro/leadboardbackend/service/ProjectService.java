package com.alejandro.leadboardbackend.service;

import com.alejandro.leadboardbackend.exception.FileUploadException;
import com.alejandro.leadboardbackend.exception.ResourceNotFoundException;
import com.alejandro.leadboardbackend.model.Project;
import com.alejandro.leadboardbackend.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con id: " + id));
    }

    @Transactional
    public Project saveProject(Project project, MultipartFile mainImage,
                               List<MultipartFile> gallery) {
        try {
            // Subir imagen principal
            if (mainImage != null && !mainImage.isEmpty()) {
                Map<String, Object> result = cloudinaryService.upload(mainImage);
                project.setMainImageUrl((String) result.get("url"));
            }

            // Subir galería si existe
            if (gallery != null && !gallery.isEmpty()) {
                List<String> galleryUrls = new ArrayList<>();
                for (MultipartFile file : gallery) {
                    Map<String, Object> result = cloudinaryService.upload(file);
                    galleryUrls.add((String) result.get("url"));
                }
                project.setGalleryUrls(galleryUrls);
            }

            return projectRepository.save(project);

        } catch (IllegalArgumentException ex) {
            // Re-lanzar excepciones de validación
            throw ex;
        } catch (IOException ex) {
            throw new FileUploadException(
                    "Error al subir las imágenes a Cloudinary", ex);
        } catch (Exception ex) {
            throw new FileUploadException(
                    "Error inesperado al procesar las imágenes", ex);
        }
    }

    @Transactional
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "No se puede eliminar. Proyecto no encontrado con id: " + id);
        }
        projectRepository.deleteById(id);
    }
}
