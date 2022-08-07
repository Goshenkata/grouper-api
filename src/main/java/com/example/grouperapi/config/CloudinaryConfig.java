package com.example.grouperapi.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cloudinary")
@Data
@NoArgsConstructor
public class CloudinaryConfig {
    @Value("cloudName")
    private String cloudName;
    @Value("apiKey")
    private String apiKey;
    @Value("apiSecret")
    private String apiSecret;
}
