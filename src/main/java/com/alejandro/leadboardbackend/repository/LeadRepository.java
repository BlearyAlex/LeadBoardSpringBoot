package com.alejandro.leadboardbackend.repository;

import com.alejandro.leadboardbackend.domain.dto.response.LeadResponseDto;
import com.alejandro.leadboardbackend.domain.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeadRepository extends JpaRepository<Lead, Long> {
    List<Lead> findByStatus(Lead.LeadStatus status);
}
