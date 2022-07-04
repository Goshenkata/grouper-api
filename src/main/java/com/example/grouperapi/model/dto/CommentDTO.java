package com.example.grouperapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO implements Serializable {
    private Long id;
    private UserDTO author;
    private Instant created;
    private String contents;
    private ImageDTO image;
    private List<CommentDTO> replies;
}
