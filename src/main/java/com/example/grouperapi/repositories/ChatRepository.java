package com.example.grouperapi.repositories;

import com.example.grouperapi.model.dto.ChatDTO;
import com.example.grouperapi.model.dto.MessageDTO;
import com.example.grouperapi.model.entities.Chat;
import com.example.grouperapi.model.entities.Message;
import com.example.grouperapi.model.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

}
