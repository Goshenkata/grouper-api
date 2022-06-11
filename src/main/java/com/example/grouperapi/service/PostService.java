package com.example.grouperapi.service;

import com.example.grouperapi.model.dto.FullPostInfoDTO;
import com.example.grouperapi.model.dto.PostFeedDTO;

import java.util.List;
import java.util.Optional;

public interface PostService {
    void seedPosts();

    FullPostInfoDTO getFullPostInfo(Long id);

    List<PostFeedDTO> getFeed(int page, int size);
}
