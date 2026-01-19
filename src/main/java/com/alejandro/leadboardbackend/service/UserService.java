package com.alejandro.leadboardbackend.service;

import com.alejandro.leadboardbackend.domain.dto.request.LoginRequestDto;
import com.alejandro.leadboardbackend.domain.dto.request.RegisterRequestDto;
import com.alejandro.leadboardbackend.domain.dto.response.LoginResponseDto;
import com.alejandro.leadboardbackend.domain.entity.User;

public interface UserService {
    LoginResponseDto login(LoginRequestDto request);
    User register(RegisterRequestDto request);
    void requestPasswordReset(String email);
    void resetPassword(String token, String newPassword);
}
