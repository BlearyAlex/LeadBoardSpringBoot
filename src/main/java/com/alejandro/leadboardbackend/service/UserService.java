package com.alejandro.leadboardbackend.service;

import com.alejandro.leadboardbackend.dto.request.LoginRequestDto;
import com.alejandro.leadboardbackend.dto.request.RegisterRequestDto;
import com.alejandro.leadboardbackend.dto.response.LoginResponseDto;
import com.alejandro.leadboardbackend.exception.business.ResourceNotFoundException;
import com.alejandro.leadboardbackend.exception.business.UserAlreadyExistsException;
import com.alejandro.leadboardbackend.model.User;
import com.alejandro.leadboardbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
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
        String accessToken = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user.getEmail()).getToken();

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
