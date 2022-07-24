package com.example.grouperapi.model.dto;

import com.example.grouperapi.model.entities.enums.PostType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
public class PostFeedDTO implements Serializable {
    private Long id;
    private PostType postType;
    private Instant created;
    private ImageDTO image;
    private UserDTO postAuthor;
    private String content;
    private String title;
    private GroupInfoDTO group;
    private Integer commentCount;
}
