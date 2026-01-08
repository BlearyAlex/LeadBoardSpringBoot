package com.alejandro.leadboardbackend.controller;

import com.alejandro.leadboardbackend.dto.ProjectRequestDto;
import com.alejandro.leadboardbackend.dto.ProjectResponseDto;
import com.alejandro.leadboardbackend.model.Project;
import com.alejandro.leadboardbackend.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjectResponseDto> create(
            @Valid @RequestPart("project") ProjectRequestDto requestDto,
            @RequestPart(value = "mainImage", required = false) MultipartFile mainImage,
            @RequestPart(value = "gallery", required = false) List<MultipartFile> gallery) {

        ProjectResponseDto savedProject = projectService.saveProject(requestDto, mainImage, gallery);

        URI location = URI.create("/api/projects" + savedProject.getId());

        return ResponseEntity.created(location).body(savedProject);
    }
}
