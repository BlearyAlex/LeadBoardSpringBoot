package com.alejandro.leadboardbackend.controller;

import com.alejandro.leadboardbackend.dto.request.ProjectRequestDto;
import com.alejandro.leadboardbackend.dto.response.ProjectResponseDto;
import com.alejandro.leadboardbackend.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjectResponseDto> create(
            @Valid @RequestPart("project") ProjectRequestDto requestDto,
            @RequestPart(value = "mainImage", required = false) MultipartFile mainImage,
            @RequestPart(value = "gallery", required = false) List<MultipartFile> gallery) {

        ProjectResponseDto savedProject = projectService.saveProject(requestDto, mainImage, gallery);

        URI location = URI.create("/api/projects/" + savedProject.getId());

        return ResponseEntity.created(location).body(savedProject);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjectResponseDto> editProject(
            @PathVariable Long id,
            @Valid @RequestPart("project") ProjectRequestDto requestDto,
            @RequestPart(value = "mainImage", required = false) MultipartFile mainImage) {

        ProjectResponseDto updatedProject = projectService.editProject(id, requestDto, mainImage);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
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
