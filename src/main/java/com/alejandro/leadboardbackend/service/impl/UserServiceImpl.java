package com.alejandro.leadboardbackend.service.impl;

import com.alejandro.leadboardbackend.domain.dto.request.LoginRequestDto;
import com.alejandro.leadboardbackend.domain.dto.request.RegisterRequestDto;
import com.alejandro.leadboardbackend.domain.dto.response.LoginResponseDto;
import com.alejandro.leadboardbackend.exception.business.ResourceNotFoundException;
import com.alejandro.leadboardbackend.exception.business.UserAlreadyExistsException;
import com.alejandro.leadboardbackend.domain.entity.User;
import com.alejandro.leadboardbackend.repository.UserRepository;
import com.alejandro.leadboardbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtServiceImpl;
    private final RefreshTokenServiceImpl refreshTokenServiceImpl;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        // 1. Autenticar
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. Buscar usuario
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", request.getEmail()));

        // 3. Generar ambos tokens
        String accessToken = jwtServiceImpl.generateToken(user);
        String refreshToken = refreshTokenServiceImpl.createRefreshToken(user.getEmail()).getToken();

        return new LoginResponseDto(accessToken, refreshToken, "Bearer");
    }

    @Override
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
}
