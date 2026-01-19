package com.alejandro.leadboardbackend.domain.dto.request;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String email;
    private String username;
    private String password;
    private String displayName;
}
