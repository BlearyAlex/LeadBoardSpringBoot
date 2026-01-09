package com.alejandro.leadboardbackend.controller;

import com.alejandro.leadboardbackend.service.ProjectImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projectsImage")
@CrossOrigin(origins = "*")
public class ProjectImageController {
    @Autowired
    private ProjectImageService projectImageService;

    @DeleteMapping("/projects/{projectId}/gallery/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGalleryImage(@PathVariable Long projectId, @PathVariable Long imageId) {
        projectImageService.deleteGalleryImage(projectId, imageId);
    }
}
