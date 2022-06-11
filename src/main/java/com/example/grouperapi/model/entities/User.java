package com.example.grouperapi.model.entities;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @ManyToMany
    Collection<Role> roles;
    @OneToMany(mappedBy = "author")
    List<Post> posts;
    @ManyToMany(mappedBy = "members")
    List<GroupEntity> groups;
    @Column
    private String pfpUrl;
}