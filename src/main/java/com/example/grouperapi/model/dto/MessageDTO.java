package com.example.grouperapi.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class MessageDTO {
    private String message;
    private UserDTO author;
    private Instant timeSent;
}
