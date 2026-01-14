package com.alejandro.leadboardbackend.controller;

import com.alejandro.leadboardbackend.dto.request.LoginRequestDto;
import com.alejandro.leadboardbackend.dto.request.RefreshRequestDto;
import com.alejandro.leadboardbackend.dto.request.RegisterRequestDto;
import com.alejandro.leadboardbackend.dto.response.LoginResponseDto;
import com.alejandro.leadboardbackend.dto.response.RegisterResponse;
import com.alejandro.leadboardbackend.model.User;
import com.alejandro.leadboardbackend.service.RefreshTokenService;
import com.alejandro.leadboardbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

   @PostMapping("/register")
   public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequestDto request) {
       User newUser = userService.register(request);
       return ResponseEntity.ok(new RegisterResponse("Usuario creado correctamente", newUser.getEmail()));
   }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        // Toda la lógica de autenticación y generación de tokens ya está en el Service
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@RequestBody RefreshRequestDto request) {
        // El servicio se encarga de validar, rotar y devolver el DTO
        return ResponseEntity.ok(refreshTokenService.refreshToken(request));
    }
}
