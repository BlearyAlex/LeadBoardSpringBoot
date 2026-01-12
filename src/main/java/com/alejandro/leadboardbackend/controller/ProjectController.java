package com.alejandro.leadboardbackend.controller;

import com.alejandro.leadboardbackend.dto.ProjectRequestDto;
import com.alejandro.leadboardbackend.dto.ProjectResponseDto;
import com.alejandro.leadboardbackend.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
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

    @PutMapping("/id")
    public ResponseEntity<ProjectResponseDto> editProject(
            @PathVariable("id") Long projectId,
            @Valid @PathVariable("project") ProjectRequestDto requestDto,
            @PathVariable(value = "mainImage", required = false) MultipartFile mainImage) {

        ProjectResponseDto updatedProject = projectService.editProject(projectId, requestDto, mainImage);
        return ResponseEntity.ok(updatedProject);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getAll() {
        List<ProjectResponseDto> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable Long id) {
        ProjectResponseDto project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }
}
