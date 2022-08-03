package com.example.grouperapi.controller;

import com.example.grouperapi.init.DBInit;
import com.example.grouperapi.model.dto.GroupInfoDTO;
import com.example.grouperapi.model.dto.PostFeedDTO;
import com.example.grouperapi.model.entities.*;
import com.example.grouperapi.model.entities.enums.PostType;
import com.example.grouperapi.repositories.*;
import com.google.gson.*;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FeedControllerTest {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private DBInit dbInit;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ModelMapper mapper;
    private GroupEntity group;
    private UserEntity user;
    private Role role;
    private Post post;
    private Post post2;

    @Test
    void getFeedNew() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/feed?page=0&size=10&sort=NEW&feedType=NONE&name="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(post.getId().intValue())))
                .andExpect(jsonPath("$[0].postType", is(post.getPostType().name())))
                .andExpect(jsonPath("$[0].content", is(post.getContent())))
                .andExpect(jsonPath("$[1].id", is(post2.getId().intValue())))
                .andExpect(jsonPath("$[1].postType", is(post2.getPostType().name())))
                .andExpect(jsonPath("$[1].content", is(post2.getContent())));
    }

    @Test
    @Transactional
    void getFeedRising() throws Exception {
        PostComment comment = new PostComment();
        comment.setPost(post2);
        comment.setImage(null);
        comment.setCreated(Instant.now());
        comment.setAuthor(user);
        comment.setContents("agreed");
        commentRepository.save(comment);

        mockMvc.perform(get("http://localhost:8080/api/feed?page=0&size=10&sort=RISING&feedType=NONE&name="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(post2.getId().intValue())))
                .andExpect(jsonPath("$[0].postType", is(post2.getPostType().name())))
                .andExpect(jsonPath("$[0].content", is(post2.getContent())))
                .andExpect(jsonPath("$[1].id", is(post.getId().intValue())))
                .andExpect(jsonPath("$[1].postType", is(post.getPostType().name())))
                .andExpect(jsonPath("$[1].content", is(post.getContent())));
    }

    @Test
    void getFeedGroupNew() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/feed?page=0&size=10&sort=NEW&feedType=GROUP&name=dogs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(post.getId().intValue())))
                .andExpect(jsonPath("$[0].postType", is(post.getPostType().name())))
                .andExpect(jsonPath("$[0].content", is(post.getContent())))
                .andExpect(jsonPath("$[1].id", is(post2.getId().intValue())))
                .andExpect(jsonPath("$[1].postType", is(post2.getPostType().name())))
                .andExpect(jsonPath("$[1].content", is(post2.getContent())));
    }

    @Test
    @Transactional
    void getFeedGroupRising() throws Exception {
        PostComment comment = new PostComment();
        comment.setPost(post2);
        comment.setImage(null);
        comment.setCreated(Instant.now());
        comment.setAuthor(user);
        comment.setContents("agreed");
        commentRepository.save(comment);

        mockMvc.perform(get("http://localhost:8080/api/feed?page=0&size=10&sort=RISING&feedType=GROUP&name=dogs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(post2.getId().intValue())))
                .andExpect(jsonPath("$[0].postType", is(post2.getPostType().name())))
                .andExpect(jsonPath("$[0].content", is(post2.getContent())))
                .andExpect(jsonPath("$[1].id", is(post.getId().intValue())))
                .andExpect(jsonPath("$[1].postType", is(post.getPostType().name())))
                .andExpect(jsonPath("$[1].content", is(post.getContent())));
    }

    @Test
    void getFeedUserNew() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/feed?page=0&size=10&sort=NEW&feedType=USER&name=user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(post.getId().intValue())))
                .andExpect(jsonPath("$[0].postType", is(post.getPostType().name())))
                .andExpect(jsonPath("$[0].content", is(post.getContent())))
                .andExpect(jsonPath("$[1].id", is(post2.getId().intValue())))
                .andExpect(jsonPath("$[1].postType", is(post2.getPostType().name())))
                .andExpect(jsonPath("$[1].content", is(post2.getContent())));
    }

    @Test
    @Transactional
    void getFeedUserRising() throws Exception {
        PostComment comment = new PostComment();
        comment.setPost(post2);
        comment.setImage(null);
        comment.setCreated(Instant.now());
        comment.setAuthor(user);
        comment.setContents("agreed");
        commentRepository.save(comment);

        mockMvc.perform(get("http://localhost:8080/api/feed?page=0&size=10&sort=RISING&feedType=USER&name=user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(post2.getId().intValue())))
                .andExpect(jsonPath("$[0].postType", is(post2.getPostType().name())))
                .andExpect(jsonPath("$[0].content", is(post2.getContent())))
                .andExpect(jsonPath("$[1].id", is(post.getId().intValue())))
                .andExpect(jsonPath("$[1].postType", is(post.getPostType().name())))
                .andExpect(jsonPath("$[1].content", is(post.getContent())));
    }


    @BeforeEach
    void setUp() {
        getGroupEntity();
        getRole();
        getUser();
        getPost();
        getPost2();
    }

    private GroupEntity getGroupEntity() {
        GroupEntity group = new GroupEntity();
        group.setName("dogs");
        group.setIconUrl(null);
        group.setDescription("dogs are cute");
        groupRepository.save(group);
        this.group = group;
        return group;
    }

    private UserEntity getUser() {
        UserEntity user = new UserEntity();
        user.setUsername("user1");
        user.setPassword(passwordEncoder.encode("UserUser@1"));
        user.setEmail("user@user1");
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        this.user = user;
        return user;
    }

    private Role getRole() {
        Role role = new Role();
        role.setName("ROLE_USER");
        roleRepository.save(role);
        this.role = role;
        return role;
    }

    private Post getPost() {
        Post post = new Post();
        post.setAuthor(user);
        post.setPostType(PostType.TEXT);
        post.setCreated(Instant.now());
        post.setGroup(group);
        post.setTitle("dogs are cute");
        post.setContent(":D");
        postRepository.save(post);
        this.post = post;
        return post;
    }

    private Post getPost2() {
        Post post2 = new Post();
        post2.setAuthor(user);
        post2.setPostType(PostType.TEXT);
        post2.setCreated(Instant.now().minus(1L, ChronoUnit.MINUTES));
        post2.setGroup(group);
        post2.setTitle("dogs are cute :)");
        post2.setContent(":D :D");
        postRepository.save(post2);
        this.post2 = post2;
        return post2;
    }

    @AfterEach
    @Transactional
    void tearDown() {
        postRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        groupRepository.deleteAll();
    }

}