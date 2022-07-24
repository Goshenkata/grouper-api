package com.example.grouperapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostCreationDTO implements Serializable {
    @NotNull(message = "Post must have a title")
    @Size(min = 1, max = 256, message = "Post must be between 1 and 256 characters")
    private String title;
    private MultipartFile image;
    @NotBlank(message = "Content cannot be blank")
    @Size(min = 1, max = 65536, message = "Content must be between 1 and 65536 characters")
    String content;
    @NotBlank(message = "No such group")
    private String groupName;
}
