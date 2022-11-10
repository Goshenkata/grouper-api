package com.example.grouperapi.model.entities;

import com.example.grouperapi.model.entities.enums.UserType;
import lombok.Data;
import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
public class UserStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Instant moment;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    private String browser;
    private String platform;
}
