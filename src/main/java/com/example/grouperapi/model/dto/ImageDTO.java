package com.example.grouperapi.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ImageDTO implements Serializable {
    private String title;
    private String url;
    private String publicId;
}
