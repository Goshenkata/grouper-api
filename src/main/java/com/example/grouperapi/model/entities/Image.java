package com.example.grouperapi.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column(nullable = false)
    private String url;
    @Column
    private String publicId;
    @OneToOne(mappedBy = "image")
    private Post post;
    @OneToOne(mappedBy = "pfp")
    private User user;
}
