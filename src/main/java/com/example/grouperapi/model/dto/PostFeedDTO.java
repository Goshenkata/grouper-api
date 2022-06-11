package com.example.grouperapi.model.dto;

import com.example.grouperapi.model.entities.enums.PostType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class PostFeedDTO {
    private Long id;
    private PostType postType;
    private Instant created;
    private String imageUrl;
    private UserDTO postAuthor;
    private String content;
    private String title;
    private String groupName;
    private Integer commentCount;
}
