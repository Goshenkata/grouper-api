package com.example.grouperapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostCreationDTO implements Serializable {
    @NotNull(message = "Post must have a title")
    private String title;
    private MultipartFile image;
    String content;
    @NotNull(message = "invalid group")
    private String groupName;
}
