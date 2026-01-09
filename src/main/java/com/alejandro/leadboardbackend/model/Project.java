package com.alejandro.leadboardbackend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String mainImageUrl; // Imagen de portada
    private String mainImagePublicId;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectImage> gallery = new ArrayList<>(); // Lista de fotos (planos, renders, fotos finales)

    // Campos adaptables
    private String category; // Ej: "Residencial", "Comercial", "Remodelación
    private String location; // Ciudad o país
    private String projectYear;
    private String clientName; // Opcional, para dar prestigio

    @ElementCollection
    private List<String> tags; // Ej: "Minimalista", "Ecológico", "Urbano"

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
