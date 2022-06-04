package com.example.grouperapi.service.impl;

import com.example.grouperapi.model.entities.Post;
import com.example.grouperapi.model.entities.enums.PostType;
import com.example.grouperapi.repositories.PostRepository;
import com.example.grouperapi.repositories.UserRepository;
import com.example.grouperapi.service.PostService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private UserRepository userRepository;

    @Override
    public void seedPosts() {
        if (postRepository.count() == 0) {
            log.info("seeding posts into the database");
            //todo
        }
    }

}
