package com.alejandro.leadboardbackend.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Leads")
@Getter
@Setter
@NoArgsConstructor
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String phone; // Importante para contacto rápido
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime receivedAt;

    @Enumerated(EnumType.STRING)
    private LeadStatus status = LeadStatus.NEW; // NEW, CONTACTED, ARCHIVED

    @PrePersist
    protected void onReceived() {
        receivedAt = LocalDateTime.now();
    }

    // Enum para los estados del prospecto
    public enum LeadStatus {
        NEW, CONTACTED, ARCHIVED
    }
}
