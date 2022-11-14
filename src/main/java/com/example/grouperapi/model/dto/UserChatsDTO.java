package com.example.grouperapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@Data
public class UserChatsDTO {
    private String name;
    private Instant lastMessageTime;
    private String lastMessage;
    private UserInfoDTO lastMessageAuthor;
}
