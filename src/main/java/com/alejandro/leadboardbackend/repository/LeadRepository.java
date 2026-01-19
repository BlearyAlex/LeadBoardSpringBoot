package com.alejandro.leadboardbackend.repository;

import com.alejandro.leadboardbackend.domain.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadRepository extends JpaRepository<Lead, Long> {
}
