package com.alejandro.leadboardbackend.domain.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LeadResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String subject;
    private String message;
    private LocalDateTime receivedAt;
    private String status;
}
