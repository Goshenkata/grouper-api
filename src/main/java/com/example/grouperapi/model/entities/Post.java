package com.example.grouperapi.model.entities;

import com.example.grouperapi.model.entities.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private PostType postType;
    @Column
    private Instant  created;
    @ManyToOne
    private User author;
    @Column(nullable = false)
    String title;
    @Column(nullable = false)
    String content;
    @OneToMany(mappedBy = "post")
    private List<PostComment> comments;
    @ManyToOne
    private GroupEntity group;
}