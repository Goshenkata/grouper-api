package com.example.grouperapi.repositories;

import com.example.grouperapi.model.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    Page<Post> findAllByOrderByCreatedDesc(Pageable pageable);
    @Query("select p from Post p where p.created >= :maxAge order by p.comments.size desc, p.created desc")
    Page<Post> findAllRising(Pageable pageable, Instant maxAge);
}
