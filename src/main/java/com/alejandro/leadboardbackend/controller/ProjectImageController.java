package com.alejandro.leadboardbackend.controller;

import com.alejandro.leadboardbackend.service.impl.ProjectImageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/projectsImage")
@CrossOrigin(origins = "*")
public class ProjectImageController {
    @Autowired
    private ProjectImageServiceImpl projectImageServiceImpl;

    @DeleteMapping("/projects/{projectId}/gallery/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGalleryImage(@PathVariable Long projectId, @PathVariable Long imageId) {
        projectImageServiceImpl.deleteGalleryImage(projectId, imageId);
    }
}
