package com.example.grouperapi.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@NoArgsConstructor
@Data
public class AddCommentDTO implements Serializable {
    @NotNull
    @Positive(message = "id ust be positive")
    private Long id;
    @NotNull(message = "response type must be POST or COMMENT")
    private ResponseType responseType;
    private MultipartFile image;
    @NotBlank(message = "content of comment must not be blank")
    private String content;
}