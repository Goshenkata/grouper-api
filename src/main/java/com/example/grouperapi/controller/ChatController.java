package com.example.grouperapi.controller;

import com.example.grouperapi.model.dto.MessageDTO;
import com.example.grouperapi.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/messages")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getChat(@RequestParam long id,
                                  @RequestParam int page,
                                  Principal principal) {
        if (chatService.isParticipantInChat(id, principal.getName())) {
            Optional<List<MessageDTO>> messages = chatService.getMessages(id, page);
            return messages.isEmpty() ? ResponseEntity.notFound().build() :
                    ResponseEntity.ok(messages.get());
        } else {
            return ResponseEntity.status(403).build();
        }
    }

}
