package com.example.grouperapi.init;

import com.example.grouperapi.repositories.RoleRepository;
import com.example.grouperapi.service.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@AllArgsConstructor
@Component
public class DBInit implements CommandLineRunner {

    private final RoleService roleService;
    private final UserService userService;
    private final GroupService groupService;
    private final PostService postService;
    private final CommentService commentService;
    private final ChatService chatService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        roleService.seedRoles();
        userService.seedUsers();
        groupService.seedGroups();
        postService.seedPosts();
        commentService.seedComments();
        chatService.seedChats();
    }


}