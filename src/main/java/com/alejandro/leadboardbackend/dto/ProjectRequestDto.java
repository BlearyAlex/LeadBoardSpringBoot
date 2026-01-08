package com.alejandro.leadboardbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Data
public class ProjectRequestDto {

    @NotBlank(message = "El titulo es obligatorio")
    @Size(min = 3, max = 150, message = "El título debe tener entre 3 y 150 caracteres")
    private String title;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 20, max = 5000, message = "La descripción debe tener entre 20 y 5000 caracteres")
    private String description;

    @NotBlank(message = "La categoría es obligatoria")
    @Pattern(regexp = "Residencial|Comercial|Remodelación|Industrial|Institucional", message = "Categoría no válida")
    private String category;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 100, message = "La ubicación no puede superar los 100 caracteres")
    private String location;

    @NotBlank(message = "El año del proyecto es obligatorio")
    @Pattern(regexp = "^(19|20)\\d{2}$", message = "El año debe ser válido (ej: 2023)")
    private String projectYear;

    @Size(max = 100, message = "El nombre del cliente no puede superar los 100 caracteres")
    private String clientName;

    @NotEmpty(message = "Debe haber al menos un tag")
    private List<@NotBlank(message = "El tag no puede estar vacío") @Size(max = 30, message = "El tag no puede superar los 30 caracteres") String> tags;
}
