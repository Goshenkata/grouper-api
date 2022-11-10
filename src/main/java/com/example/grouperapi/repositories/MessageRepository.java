package com.example.grouperapi.repositories;

import com.example.grouperapi.model.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
