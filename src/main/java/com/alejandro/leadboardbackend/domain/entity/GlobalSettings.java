package com.alejandro.leadboardbackend.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class GlobalSettings {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contactPhone;
    private String contactEmail;
    private String officeAddress;
    private String instagramUrl;
    private String linkedinUrl;
    private String aboutMeSummary; // Resumen corto para el footer
    private String cvResumeUrl;    // Link al PDF en Cloudinary
}
