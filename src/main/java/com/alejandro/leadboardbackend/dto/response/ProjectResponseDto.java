package com.alejandro.leadboardbackend.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ProjectResponseDto {
    private Long id;
    private String title;
    private String description;
    private String mainImageUrl;
    private String mainImagePublicId;
    private List<ProjectImageResponseDto> galleryUrls;
    private String category;
    private String location;
    private String projectYear;
    private String clientName;
    private List<String> tags;
}

