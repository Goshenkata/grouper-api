package com.example.grouperapi.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@ToString
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    @JoinTable(name = "users_chats",
    joinColumns = @JoinColumn(name = "chats_id"),
    inverseJoinColumns = @JoinColumn(name = "user_entity_id"))
    private List<UserEntity> users;
    @OneToMany
    private List<Message> messages;
    Instant lastMessageTime;
    @Column(nullable = true)
    private String name;
}
