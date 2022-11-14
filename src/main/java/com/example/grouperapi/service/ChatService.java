package com.example.grouperapi.service;

import com.example.grouperapi.model.dto.MessageDTO;
import com.example.grouperapi.model.entities.Chat;
import com.example.grouperapi.model.entities.Message;
import com.example.grouperapi.model.entities.UserEntity;
import com.example.grouperapi.repositories.ChatRepository;
import com.example.grouperapi.repositories.MessageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ChatService {
    private final ModelMapper modelMapper;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserService userService;

    public void seedChats() {
        if (messageRepository.count() == 0) {
            log.info("initialising chats");
            createUserAdminChat();
            createUserUser1Chat();
            log.info("chats initialised");
        }
    }

    private void createUserUser1Chat() {
        List<UserEntity> users = new ArrayList<>();
        UserEntity user = userService.getUserByUsername("user");
        UserEntity user1 = userService.getUserByUsername("user1");
        users.add(user);
        users.add(user1);

        Chat chat = new Chat();
        chat.setUsers(users);
        chat.setName(null);

        List<Message> messages = seedMessagesUserUser1(user, user1, chat);
        chat.setMessages(messages);
        chat.setLastMessageTime(messages.get(messages.size() - 1).getTimeSent());
        chatRepository.save(chat);
    }

    private List<Message> seedMessagesUserUser1(UserEntity user, UserEntity user1, Chat chat) {
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i % 2 == 0) {
                messages.add(createMessage("Message " + i, user, chat));
            } else {
                messages.add(createMessage("Message " + i, user1, chat));
            }
        }
        return messages;
    }

    private void createUserAdminChat() {
        List<UserEntity> users = new ArrayList<>();
        UserEntity admin = userService.getUserByUsername("admin");
        UserEntity user = userService.getUserByUsername("user");
        users.add(admin);
        users.add(user);

        Chat chat = new Chat();
        chat.setUsers(users);
        chat.setName(null);

        List<Message> messages = seedMessagesUserAdmin(user, admin, chat);
        chat.setMessages(messages);
        chat.setLastMessageTime(messages.get(messages.size() - 1).getTimeSent());
        chatRepository.save(chat);
    }

    private List<Message> seedMessagesUserAdmin(UserEntity user, UserEntity admin, Chat chat) {
        List<Message> messages = new ArrayList<>();
        messages.add(createMessage("Hii :3", admin, chat));
        messages.add(createMessage("Hii :p", user, chat));
        messages.add(createMessage("You're banned", admin, chat));
        messages.add(createMessage("(for spamming,chat)", admin, chat));
        messages.add(createMessage("walterwhitecollapse.gif", user, chat));
        return messages;
    }

    private Message createMessage(String messageString, UserEntity author, Chat chat) {
        Message message = new Message();
        message.setMessage(messageString);
        message.setTimeSent(Instant.now());
        message.setAuthor(author);
        message.setChat(chat);
        messageRepository.save(message);
        return message;
    }

    public Optional<List<MessageDTO>> getMessages(long id, int page) {
        Optional<Chat> chat = chatRepository.findById(id);
        if(chat.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(messageRepository
                    .findAllByChatOrderByTimeSent(chat.get(), PageRequest.of(page, 30))
                    .stream().map(message -> modelMapper.map(message, MessageDTO.class))
                    .toList());
        }
    }

    public boolean isParticipantInChat(long chatId, String username) {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isEmpty()) {
            return false;
        } else {
            UserEntity principal = userService.getUserByUsername(username);
            return chat.get().getUsers().contains(principal);
        }
    }
}
