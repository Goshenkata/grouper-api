package com.example.grouperapi.controller;

import com.example.grouperapi.model.dto.ChatDTO;
import com.example.grouperapi.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getChat(@RequestParam long id,
                                                 @RequestParam int page) {
        return ResponseEntity.ok(chatService.getChat(id, page));
    }
}
