package com.example.grouperapi.service;

import com.example.grouperapi.model.entities.Chat;
import com.example.grouperapi.model.entities.Message;
import com.example.grouperapi.model.entities.UserEntity;
import com.example.grouperapi.repositories.ChatRepository;
import com.example.grouperapi.repositories.MessageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserService userService;
    public void seedChats() {
        if(messageRepository.count() == 0) {
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

        List<Message> messages = seedMessagesUserUser1(user, user1);
        chat.setMessages(messages);
        chat.setLastMessageTime(messages.get(messages.size() -1).getTimeSent());
        chatRepository.save(chat);
    }

    private List<Message> seedMessagesUserUser1(UserEntity user, UserEntity user1) {
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i % 2 ==0) {
                messages.add(createMessage("Message " + i, user));
            } else {
                messages.add(createMessage("Message " + i, user1));
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

        List<Message> messages = seedMessagesUserAdmin(user, admin);
        chat.setMessages(messages);
        chat.setLastMessageTime(messages.get(messages.size() -1).getTimeSent());
        chatRepository.save(chat);
    }

    private List<Message> seedMessagesUserAdmin(UserEntity user, UserEntity admin) {
        List<Message> messages = new ArrayList<>();
        messages.add(createMessage("Hii :3", admin));
        messages.add(createMessage("Hii :p", user));
        messages.add(createMessage("You're banned", admin));
        messages.add(createMessage("(for spamming)", admin));
        messages.add(createMessage("walterwhitecollapse.gif", user));
        return messages;
    }
    private Message createMessage(String messageString, UserEntity author) {
        Message message = new Message();
        message.setMessage(messageString);
        message.setTimeSent(Instant.now());
        message.setAuthor(author);
        messageRepository.save(message);
        return message;
    }

    public List<Message> getChat(long id, int page) {
        throw new RuntimeException("TODO");
    }
}
