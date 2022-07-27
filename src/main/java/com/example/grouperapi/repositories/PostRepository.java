package com.example.grouperapi.repositories;

import com.example.grouperapi.model.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.time.Instant;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    Page<Post> findAllByOrderByCreatedDesc(Pageable pageable);
    @Query("select p from Post p where p.created >= :maxAge order by p.comments.size desc, p.created desc")
    Page<Post> findAllRising(Pageable pageable, Instant maxAge);

    //group
    Page<Post> findAllByGroupNameOrderByCreatedDesc(String group_name, Pageable pageable);

    @Query("select p from Post p where p.created >= :maxAge and p.group.name = :name order by p.comments.size desc, p.created desc")
    Page<Post> findAllByGroupRising(Pageable pageable, Instant maxAge, String name);

    //user

    //group
    Page<Post> findAllByAuthorUsernameOrderByCreatedDesc(String author_username, Pageable pageable);

    @Query("select p from Post p where p.created >= :maxAge and p.author.username = :name order by p.comments.size desc, p.created desc")
    Page<Post> findAllByUserRising(Pageable pageable, Instant maxAge, String name);
}
