package com.example.grouperapi.repositories;

import com.example.grouperapi.model.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    //todo c.participants.name
    List<Chat> findAllByOrderByLastMessageTime();

}
