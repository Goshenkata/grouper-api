package com.example.grouperapi.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Data
@NoArgsConstructor
@ToString
public class MessageDTO {
    private String message;
    private UserInfoDTO author;
    private Instant timeSent;
}
