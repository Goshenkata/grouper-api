package com.example.grouperapi.service;

import com.example.grouperapi.model.dto.FeedType;
import com.example.grouperapi.model.dto.PostFeedDTO;
import com.example.grouperapi.model.dto.SortType;
import com.example.grouperapi.repositories.PostRepository;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class FeedService {
    private PostRepository postRepository;
    private ModelMapper mapper;

    public List<PostFeedDTO> getFeed(int page, int size, SortType sort, FeedType feedType, String name) {
        switch (feedType) {
            case USER -> {
                return getUserFeed(page, size, sort, name);
            }
            case GROUP -> {
                return getGroupFeed(page, size, sort, name);
            }
            case NONE -> {
                return getNoneFeed(page, size, sort);
            }
            default -> throw new IllegalStateException("Unexpected value: " + feedType);
        }
    }

    private List<PostFeedDTO> getUserFeed(int page, int size, SortType sort, String name) {
        if (sort == SortType.NEW) {
            return postRepository.findAllByAuthorUsernameOrderByCreatedDesc(name, PageRequest.of(page, size))
                    .map(post -> mapper.map(post, PostFeedDTO.class))
                    .toList();
        } else {
            return postRepository.findAllByUserRising(PageRequest.of(page, size),Instant.now().minus(7, ChronoUnit.DAYS), name)
                    .map(post -> mapper.map(post, PostFeedDTO.class))
                    .toList();
        }
    }

    private List<PostFeedDTO> getGroupFeed(int page, int size, SortType sort, String name) {
        if (sort == SortType.NEW) {
            return postRepository.findAllByGroupNameOrderByCreatedDesc(name, PageRequest.of(page, size))
                    .map(post -> mapper.map(post, PostFeedDTO.class))
                    .toList();
        } else {
            return postRepository.findAllByGroupRising(PageRequest.of(page, size),Instant.now().minus(7, ChronoUnit.DAYS), name)
                    .map(post -> mapper.map(post, PostFeedDTO.class))
                    .toList();
        }
    }

    private List<PostFeedDTO> getNoneFeed(int page, int size, SortType sort) {
        if (sort == SortType.NEW) {
            return postRepository.findAllByOrderByCreatedDesc(PageRequest.of(page, size))
                    .map(post -> mapper.map(post, PostFeedDTO.class))
                    .toList();
        } else {
            return postRepository.findAllRising(PageRequest.of(page, size), Instant.now().minus(7, ChronoUnit.DAYS))
                    .map(post -> mapper.map(post, PostFeedDTO.class))
                    .toList();
        }
    }
}
