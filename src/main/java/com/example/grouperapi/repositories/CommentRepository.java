package com.example.grouperapi.repositories;

import com.example.grouperapi.model.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
