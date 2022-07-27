package com.example.grouperapi.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfoDTO {
    private String imageUrl;
    private String name;
    private String description;
}
