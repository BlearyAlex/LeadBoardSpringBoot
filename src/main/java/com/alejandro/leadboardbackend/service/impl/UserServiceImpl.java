package com.alejandro.leadboardbackend.service.impl;

import com.alejandro.leadboardbackend.domain.dto.request.LoginRequestDto;
import com.alejandro.leadboardbackend.domain.dto.request.RegisterRequestDto;
import com.alejandro.leadboardbackend.domain.dto.response.LoginResponseDto;
import com.alejandro.leadboardbackend.domain.entity.PasswordResetToken;
import com.alejandro.leadboardbackend.domain.entity.User;
import com.alejandro.leadboardbackend.exception.business.*;
import com.alejandro.leadboardbackend.repository.PasswordResetTokenRepository;
import com.alejandro.leadboardbackend.repository.UserRepository;
import com.alejandro.leadboardbackend.service.EmailService;
import com.alejandro.leadboardbackend.service.UserService;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtServiceImpl;
    private final RefreshTokenServiceImpl refreshTokenServiceImpl;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtServiceImpl jwtServiceImpl, RefreshTokenServiceImpl refreshTokenServiceImpl, AuthenticationManager authenticationManager, PasswordResetTokenRepository tokenRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtServiceImpl = jwtServiceImpl;
        this.refreshTokenServiceImpl = refreshTokenServiceImpl;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto request) {
        try {
            // 1. Intentamos autenticar al usuario con el manager de autenticación
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException ex) {
            // Lanzamos una excepción personalizada si las credenciales son incorrectas
            throw new InvalidCredentialsException("Email o contraseña incorrectos");
        } catch (LockedException ex) {
            // Lanzamos una excepción personalizada si la cuenta está bloqueada
            throw new AccountLockedException("La cuenta está bloqueada");
        } catch (DisabledException ex) {
            // Si la cuenta está deshabilitada
            throw new AccountLockedException("La cuenta está deshabilitada");
        }

        // 2. Buscar usuario
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", request.getEmail()));

        // 3. Generar ambos tokens
        String accessToken = jwtServiceImpl.generateToken(user);
        String refreshToken = refreshTokenServiceImpl.createRefreshToken(user.getEmail()).getToken();

        return new LoginResponseDto(accessToken, refreshToken, "Bearer");
    }

    @Override
    @Transactional
    public User register(RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername()); // O usa email si prefieres
        user.setDisplayName(request.getDisplayName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        // 1. Verificar si el usuario existe
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // 2. Generar y guardar token (borrar previos si existen)
        String token = UUID.randomUUID().toString();
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        tokenRepository.save(myToken);

        // 3. Enviar email
        String link = "http://localhost:5173/reset-password?token=" + token;
        emailService.sendResetPasswordEmail(user.getEmail(), link);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        // 1. Validar token
        PasswordResetToken passToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidOperationException("Token inválido"));

        // 2. Verificar expiración
        if (passToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(passToken);
            throw new InvalidOperationException("El token ha expirado");
        }

        // 3. Actualizar contraseña del usuario
        User user = passToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // 4. Limpiar token usado
        tokenRepository.delete(passToken);
    }
}
