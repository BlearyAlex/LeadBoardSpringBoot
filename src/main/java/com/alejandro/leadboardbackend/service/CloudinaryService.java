package com.alejandro.leadboardbackend.service;

import com.alejandro.leadboardbackend.domain.dto.response.CloudinaryResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {

    CloudinaryResponseDto upload(MultipartFile multipartFile);

    void delete(String publicId);
}
