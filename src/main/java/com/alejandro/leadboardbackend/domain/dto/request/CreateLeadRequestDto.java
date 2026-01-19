package com.alejandro.leadboardbackend.domain.dto.request;

import lombok.Data;

@Data
public class CreateLeadRequestDto {
    private String fullName;
    private String email;
    private String phone;
    private String subject;
    private String message;
    private String status;
}
