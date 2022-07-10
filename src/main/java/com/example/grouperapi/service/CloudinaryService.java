package com.example.grouperapi.service;

import com.cloudinary.Cloudinary;
import com.example.grouperapi.model.dto.ImageDTO;
import com.example.grouperapi.model.entities.Image;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class CloudinaryService {
    Cloudinary cloudinary;

    public Image postImage(MultipartFile multipartFile) throws IOException {
        if (multipartFile != null) {
            File tempFile = File.createTempFile("temp-file", multipartFile.getOriginalFilename());
            multipartFile.transferTo(tempFile);
            Map<String, String> uploadResult = cloudinary.uploader()
                    .upload(tempFile, Map.of());
            String url = uploadResult.getOrDefault("url", "https://res.cloudinary.com/dcsi2qq6y/image/upload/v1655068682/52-520194_error-404-page-was-not-found-news-http_deztar.jpg");
            String publicId = uploadResult.get("public_id");
            Image image = new Image();
            image.setPublicId(publicId);
            image.setUrl(url);
            return image;
        }
        log.debug("multipart is empty");
        return null;
    }
}
