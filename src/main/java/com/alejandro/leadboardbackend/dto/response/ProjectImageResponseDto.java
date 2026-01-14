package com.alejandro.leadboardbackend.dto.response;

import lombok.Data;

@Data
public class ProjectImageResponseDto {
    private Long id;
    private String url;
    protected String publicId;
}
