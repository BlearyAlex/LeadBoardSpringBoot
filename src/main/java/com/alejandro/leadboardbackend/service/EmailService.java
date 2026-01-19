package com.alejandro.leadboardbackend.service;

public interface EmailService {
    void sendResetPasswordEmail(String to, String resetLink);
}
