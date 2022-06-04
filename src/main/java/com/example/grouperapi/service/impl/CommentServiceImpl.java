package com.example.grouperapi.service.impl;

import com.example.grouperapi.model.entities.PostComment;
import com.example.grouperapi.repositories.CommentRepository;
import com.example.grouperapi.repositories.PostRepository;
import com.example.grouperapi.service.CommentService;
import com.example.grouperapi.service.PostService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    @Override
    public void seedComments() {
        commentsLinux();
    }

    private void commentsLinux() {
        postComment1();
        postComment2();
    }

    private void postComment1() {
        PostComment postComment = new PostComment();
        postComment.setPost(postRepository.getById(1L));
    }
}
