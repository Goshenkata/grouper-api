package com.example.grouperapi.model.dto;

import com.example.grouperapi.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/comment")
@AllArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/add")
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
}