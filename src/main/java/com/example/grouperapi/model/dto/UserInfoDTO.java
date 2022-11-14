package com.example.grouperapi.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class UserInfoDTO {
    private String imageUrl;
    private String name;
    private String description;
}
