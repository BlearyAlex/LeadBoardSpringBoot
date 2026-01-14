package com.alejandro.leadboardbackend.dto.request;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
