package com.example.grouperapi.repositories;

import com.example.grouperapi.model.entities.Comment;
import com.example.grouperapi.model.entities.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByRepliesContaining(Comment reply);
}
