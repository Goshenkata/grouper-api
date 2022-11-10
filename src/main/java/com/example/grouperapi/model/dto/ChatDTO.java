package com.example.grouperapi.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@NoArgsConstructor
@Data
public class ChatDTO {
    private List<UserDTO> participants;
    private List<MessageDTO> messages;
    private String name;
}
