package com.alejandro.leadboardbackend.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface IJwtService {
    // Extrae el username/email del token
    String extractUsername(String token);

    // Valida que el token sea correcto y coincida con el usuario
    boolean isTokenValid(String token, UserDetails userDetails);

    // (Opcional) Genera un token para un usuario
    String generateToken(UserDetails userDetails);
}
