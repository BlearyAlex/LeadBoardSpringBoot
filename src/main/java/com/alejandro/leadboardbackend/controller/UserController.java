package com.alejandro.leadboardbackend.controller;

import com.alejandro.leadboardbackend.dto.LoginDto;
import com.alejandro.leadboardbackend.dto.UserDto;
import com.alejandro.leadboardbackend.model.User;
import com.alejandro.leadboardbackend.service.UserService;
import com.alejandro.leadboardbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto user) {
        try {
            User newUser = userService.registerUser(user.getUsername(), user.getPassword());
            return ResponseEntity.ok("Usuario creado: " + newUser.getUsername());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto user) {
        if (userService.authenticate(user.getUsername(), user.getPassword())) {
            String token = JwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok("Bearer " + token);
        } else {
            return ResponseEntity.status(401).body("Usuario o contraseña incorrecta");
        }
    }
}
