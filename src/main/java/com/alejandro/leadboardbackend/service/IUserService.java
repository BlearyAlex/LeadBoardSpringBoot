package com.alejandro.leadboardbackend.service;

import com.alejandro.leadboardbackend.dto.request.LoginRequestDto;
import com.alejandro.leadboardbackend.dto.request.RegisterRequestDto;
import com.alejandro.leadboardbackend.dto.response.LoginResponseDto;
import com.alejandro.leadboardbackend.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUserService {
    LoginResponseDto login(LoginRequestDto request);
    User register(RegisterRequestDto request);
}
