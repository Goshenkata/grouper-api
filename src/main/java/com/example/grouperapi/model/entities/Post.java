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
    @Column(nullable = false)
    private Instant  created;
    @ManyToOne
    private User author;
    @Column(nullable = false)
    String title;
    @OneToOne
    private Image image;
    @Lob
    @Column
    String content;
    @Column(nullable = false)
    Integer commentCount;
    @OneToMany(mappedBy = "post")
    private List<PostComment> comments;
    @ManyToOne
    private GroupEntity group;
}