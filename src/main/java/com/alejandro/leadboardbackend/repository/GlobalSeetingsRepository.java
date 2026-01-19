package com.alejandro.leadboardbackend.repository;

import com.alejandro.leadboardbackend.domain.entity.GlobalSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalSeetingsRepository extends JpaRepository<GlobalSettings, Long> {
}
