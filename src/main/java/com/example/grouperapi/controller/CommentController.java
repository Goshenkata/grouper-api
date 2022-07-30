package com.example.grouperapi.controller;

import com.example.grouperapi.model.dto.AddCommentDTO;
import com.example.grouperapi.service.CommentService;
import com.example.grouperapi.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

@RestController
@RequestMapping("/api/comment")
@AllArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;

    @PostMapping(value = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> addComment(@Valid @ModelAttribute AddCommentDTO addCommentDTO,
                                           BindingResult bindingResult,
                                           Principal principal) throws IOException {
        if (bindingResult.hasErrors()) {
            log.debug("invalid input");
            return ResponseEntity.badRequest().build();
        }
        commentService.addComment(addCommentDTO, principal.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity delete(@PathVariable Long id,
                                 Principal principal) throws IOException {
        if (principal.getName().equals(commentService.getAuthorName(id))
                || userService.isAdmin(principal.getName())) {
            commentService.delete(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}