package com.alejandro.leadboardbackend.service;

import com.alejandro.leadboardbackend.dto.request.RefreshRequestDto;
import com.alejandro.leadboardbackend.dto.response.LoginResponseDto;
import com.alejandro.leadboardbackend.exception.business.TokenRefreshException;
import com.alejandro.leadboardbackend.model.RefreshToken;
import com.alejandro.leadboardbackend.model.User;
import com.alejandro.leadboardbackend.repository.RefreshTokenRepository;
import com.alejandro.leadboardbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public RefreshToken createRefreshToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado."));

        // Opcional: Eliminar tokens viejos del usuario antes de crear uno nuevo
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpiration));

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token expirado. Por favor, inicie sesión de nuevo.");
        }
        return token;
    }

    @Transactional
    public LoginResponseDto refreshToken(RefreshRequestDto request) {
        // 1. Buscamos el token que viene del cliente
        RefreshToken oldToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token no encontrado"));

        // 2. Verificamos si expiró
        verifyExpiration(oldToken);

        // 3. Obtenemos el usuario ANTES de borrar nada
        User user = oldToken.getUser();

        // 4. ROTACIÓN SEGURA: Borramos todos los tokens del usuario (incluyendo el actual)
        // Esto limpia la tabla y evita el doble borrado del mismo objeto
        refreshTokenRepository.deleteByUser(user);

        // 5. Generamos los nuevos tokens
        String newAccessToken = jwtService.generateToken(user);
        RefreshToken newRefreshToken = createRefreshToken(user.getEmail());

        return new LoginResponseDto(
                newAccessToken,
                newRefreshToken.getToken(),
                "Bearer"
        );
    }
}
