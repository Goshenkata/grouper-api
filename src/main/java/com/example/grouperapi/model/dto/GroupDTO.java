package com.example.grouperapi.model.dto;

import com.example.grouperapi.model.entities.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GroupDTO {
    private String name;
    private String iconUrl;
    private String description;
}
