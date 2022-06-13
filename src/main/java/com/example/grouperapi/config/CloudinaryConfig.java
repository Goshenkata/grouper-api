package com.example.grouperapi.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cloudinary")
@Data
@NoArgsConstructor
public class CloudinaryConfig {
    private String cloudName;
    private String apiKey;
    private String apiSecret;
}
