package com.example.grouperapi.service;

import com.cloudinary.Cloudinary;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
public class CloudinaryService {
    Cloudinary cloudinary;

    public String postImage(MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile("temp-file", multipartFile.getOriginalFilename());
        multipartFile.transferTo(tempFile);
        Map<String, String> uploadResult = cloudinary.uploader()
                .upload(tempFile, Map.of());
        return uploadResult.getOrDefault("url", "https://res.cloudinary.com/dcsi2qq6y/image/upload/v1655068682/52-520194_error-404-page-was-not-found-news-http_deztar.jpg");
    }
}
