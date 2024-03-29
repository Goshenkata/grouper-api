package com.example.grouperapi.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    String name;

    @Column
    String iconUrl;
    @Column(columnDefinition = "TEXT")
    String description;

//    @ManyToMany
//    @JoinTable(name = "members_groups",
//    joinColumns = @JoinColumn(name = "group_entity_id"),
//    inverseJoinColumns = @JoinColumn(name = "user_id"))
//    private List<User> members;

    @OneToMany(mappedBy = "group")
    private List<Post> posts;
}
