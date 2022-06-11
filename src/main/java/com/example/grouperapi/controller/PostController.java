package com.example.grouperapi.controller;


import com.example.grouperapi.model.dto.FullPostInfoDTO;
import com.example.grouperapi.model.dto.PostFeedDTO;
import com.example.grouperapi.service.PostService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/post/")
@AllArgsConstructor
public class PostController {
    private final PostService postService;

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

}
