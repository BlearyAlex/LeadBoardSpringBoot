package com.alejandro.leadboardbackend.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

