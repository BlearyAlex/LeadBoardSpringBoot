package com.alejandro.leadboardbackend.domain.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ForgotPasswordRequestDto {
    private String email;
}
