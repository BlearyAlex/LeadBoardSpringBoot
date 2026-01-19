package com.alejandro.leadboardbackend.service;

import com.alejandro.leadboardbackend.domain.dto.request.ProjectRequestDto;
import com.alejandro.leadboardbackend.domain.dto.response.ProjectResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProjectService {
    ProjectResponseDto saveProject(ProjectRequestDto requestDto, MultipartFile mainImage, List<MultipartFile> gallery);

    ProjectResponseDto editProject(Long projectId, ProjectRequestDto requestDto, MultipartFile mainImage);

    void deleteProject(Long projectId);

    List<ProjectResponseDto> getAllProjects();

    ProjectResponseDto getProjectById(Long projectId);
}
