package com.alejandro.leadboardbackend.service.impl;

import com.alejandro.leadboardbackend.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendResetPasswordEmail(String to, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Recuperación de Contraseña - LeadBoard");
        message.setText("Para restablecer tu contraseña, haz clic en el siguiente enlace: " + resetLink);

        mailSender.send(message);
    }
}
