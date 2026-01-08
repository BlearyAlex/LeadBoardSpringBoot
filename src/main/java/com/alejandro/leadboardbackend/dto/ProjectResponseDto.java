package com.alejandro.leadboardbackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProjectResponseDto {
    private Long id;
    private String title;
    private String description;
    private String mainImageUrl;
    private List<String> galleryUrls;
    private String category;
    private String location;
    private String projectYear;
    private String clientName;
    private List<String> tags;
}
