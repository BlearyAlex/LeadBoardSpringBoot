package com.alejandro.leadboardbackend.service;

import com.alejandro.leadboardbackend.dto.ProjectRequestDto;
import com.alejandro.leadboardbackend.dto.ProjectResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProjectService {
    ProjectResponseDto saveProject(ProjectRequestDto requestDto, MultipartFile mainImage, List<MultipartFile> gallery);
}
