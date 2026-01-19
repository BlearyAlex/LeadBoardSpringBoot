package com.alejandro.leadboardbackend.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshRequestDto {

    @NotBlank(message = "El refresh token es obligatorio")
    private String refreshToken;
}
