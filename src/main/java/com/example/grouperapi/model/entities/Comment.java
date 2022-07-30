package com.example.grouperapi.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@ToString
public abstract class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.REMOVE)
    private User author;
    @Column(nullable = false)
    private Instant created;
    @OneToOne
    private Image image;
    @Lob
    @Column(nullable = false)
    String contents;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Reply> replies;
}