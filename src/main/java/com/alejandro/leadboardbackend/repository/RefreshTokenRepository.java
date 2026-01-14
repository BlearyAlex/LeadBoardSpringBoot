package com.alejandro.leadboardbackend.repository;

import com.alejandro.leadboardbackend.model.RefreshToken;
import com.alejandro.leadboardbackend.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    
    Optional<RefreshToken> findByToken(String token);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM RefreshToken r WHERE r.user = ?1")
    void deleteByUser(User user);
}
