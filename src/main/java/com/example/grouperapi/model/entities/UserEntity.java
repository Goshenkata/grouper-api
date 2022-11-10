package com.example.grouperapi.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false)
    private String password;
    @ManyToMany
    Collection<Role> roles;
    @OneToMany(mappedBy = "author")
    List<Post> posts;
//    @ManyToMany(mappedBy = "members")
//    List<GroupEntity> groups;
    @ManyToMany(mappedBy = "users")
	private List<Chat> chats;
    @OneToOne
    private Image pfp;
}
