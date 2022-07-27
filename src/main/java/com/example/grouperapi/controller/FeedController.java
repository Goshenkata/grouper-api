package com.example.grouperapi.controller;

import com.example.grouperapi.model.dto.FeedType;
import com.example.grouperapi.model.dto.PostFeedDTO;
import com.example.grouperapi.model.dto.SortType;
import com.example.grouperapi.service.FeedService;
import com.example.grouperapi.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/")
public class FeedController {
    private final FeedService feedService;

    @GetMapping("feed")
    public ResponseEntity<List<PostFeedDTO>> getPostFeed(@RequestParam int page,
                                                         @RequestParam int size,
                                                         @RequestParam SortType sort,
                                                         @RequestParam FeedType feedType,
                                                         @RequestParam String name) {
        return ResponseEntity.ok(feedService.getFeed(page, size, sort, feedType, name));
    }
}
