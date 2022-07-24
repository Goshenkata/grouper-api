package com.example.grouperapi.controller;


import com.example.grouperapi.model.dto.FullPostInfoDTO;
import com.example.grouperapi.model.dto.PostCreationDTO;
import com.example.grouperapi.model.dto.PostFeedDTO;
import com.example.grouperapi.model.dto.StringJsonDTO;
import com.example.grouperapi.service.GroupService;
import com.example.grouperapi.service.PostService;
import jdk.jfr.ContentType;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/post/")
@AllArgsConstructor
public class PostController {
    private final PostService postService;
    private final GroupService groupService;

    @GetMapping("{id}")
    public ResponseEntity<FullPostInfoDTO> getPostFullPostInfo(@PathVariable("id") Long id) {
        FullPostInfoDTO fullPostInfoDTO = postService.getFullPostInfo(id);
        if (fullPostInfoDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fullPostInfoDTO);
    }

    @GetMapping("feed")
    public ResponseEntity<List<PostFeedDTO>> getPostFeed(@RequestParam("page") int page, @RequestParam("size") int size) {
        return ResponseEntity.ok(postService.getFeed(page, size));
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

}
