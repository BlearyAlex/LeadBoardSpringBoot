package com.alejandro.leadboardbackend.repository;

import com.alejandro.leadboardbackend.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByCategory(String category);

    @Query("""
    SELECT DISTINCT p
    FROM Project p
    LEFT JOIN FETCH p.gallery
    ORDER BY p.createdAt DESC
    """)
    List<Project> findAllWithImages();

    @Query("""
    SELECT DISTINCT p
    FROM Project p
    LEFT JOIN FETCH p.gallery
    WHERE p.id = :id
    """)
    Optional<Project> findByIdWithGallery(Long id);
}
