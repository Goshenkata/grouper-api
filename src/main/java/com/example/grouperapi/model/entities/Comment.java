package com.example.grouperapi.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor @Entity
public abstract class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User author;
    @Column(nullable = false)
    private Instant created;
    @Lob
    @Column(nullable = false)
    String contents;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Reply> replies;
}