package com.example.grouperapi.controller;


import com.example.grouperapi.model.dto.*;
import com.example.grouperapi.service.GroupService;
import com.example.grouperapi.service.PostService;
import com.example.grouperapi.service.UserService;
import jdk.jfr.ContentType;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/post/")
@AllArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final GroupService groupService;

    @GetMapping("{id}")
    public ResponseEntity<FullPostInfoDTO> getPostFullPostInfo(@PathVariable("id") Long id) {
        FullPostInfoDTO fullPostInfoDTO = postService.getFullPostInfo(id);
        if (fullPostInfoDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fullPostInfoDTO);
    }


    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StringJsonDTO> addPost(@Valid @ModelAttribute PostCreationDTO dto,
                                                 BindingResult bindingResult,
                                                 Principal principal) throws IOException {
        //validation
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new StringJsonDTO(bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }
        if (groupService.findByGroupByName(dto.getGroupName()).isEmpty()) {
            return ResponseEntity.badRequest().body(new StringJsonDTO("No such group"));
        }
        //max image size == 10mb
        if (dto.getImage() != null && dto.getImage().getSize() > 10_000_000) {
            return ResponseEntity.status(413).body(new StringJsonDTO("Image too large, must not exceed 10 mb"));
        }
        Long postId = postService.createPost(dto, principal.getName());
        return ResponseEntity
                .created(URI.create("http://localhost:8080/api/post/" + postId))
                .body(new StringJsonDTO("post/" + postId));
    }


    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity deletePost(@PathVariable Long id,
                                     Principal principal) throws IOException {
        // if the principal is the author of the post or an admin
        if (principal.getName().equals(postService.getAuthor(id))
                || userService.isAdmin(principal.getName())) {
            postService.delete(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}