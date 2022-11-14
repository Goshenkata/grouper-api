package com.example.grouperapi.repositories;

import com.example.grouperapi.model.entities.Chat;
import com.example.grouperapi.model.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findAllByChatOrderByTimeSent(Chat chat, Pageable pageable);
}
