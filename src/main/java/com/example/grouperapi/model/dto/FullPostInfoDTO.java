package com.example.grouperapi.model.dto;

import com.example.grouperapi.model.entities.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullPostInfoDTO implements Serializable {
    private Long id;
    private PostType postType;
    private Instant created;
    private UserDTO postAuthor;
    private String content;
    private String title;
    private GroupInfoDTO group;
    private Integer commentCount;
    private ImageDTO image;
    private List<CommentDTO> comments;
}