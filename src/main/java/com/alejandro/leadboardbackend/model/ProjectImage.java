package com.alejandro.leadboardbackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ProjectImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private String publicId;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
