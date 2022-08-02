package com.example.grouperapi.controller;

import com.example.grouperapi.init.DBInit;
import com.example.grouperapi.model.dto.ResponseType;
import com.example.grouperapi.model.entities.*;
import com.example.grouperapi.model.entities.enums.PostType;
import com.example.grouperapi.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser("user1")
class CommentControllerTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    RoleRepository roleRepository;
    @MockBean
    DBInit dbInit;

    private GroupEntity group;
    private UserEntity user;
    private Role role;
    private Post post;
    @Autowired
    CommentRepository commentRepository;

    //POST REPLY NEW
    @Test
    @Transactional
    void postCommentReply() throws Exception {
        mockMvc.perform(multipart("http://localhost:8080//api/comment/add")
                        .param("id", post.getId().toString())
                        .param("responseType", ResponseType.POST.name())
                        .param("content", "content"))
                .andExpect(status().isOk());
        Assertions.assertEquals(1, commentRepository.count());
        Comment comment = commentRepository.findAll().get(0);
        Assertions.assertEquals("content", comment.getContents());
        Assertions.assertEquals("user1", comment.getAuthor().getUsername());
    }

    @Test
    void postInvalidReply() throws Exception {
        mockMvc.perform(multipart("http://localhost:8080//api/comment/add")
                        .param("id", post.getId().toString())
                        .param("responseType", ResponseType.POST.name())
                        .param("content", ""))
                .andExpect(status().isBadRequest());
        Assertions.assertEquals(0, commentRepository.count());
    }

    //COMMENT REPLY NEW
    @Test
    @Transactional
    void commentReply() throws Exception {

        PostComment postComment = new PostComment();
        postComment.setContents("content");
        postComment.setAuthor(user);
        postComment.setCreated(Instant.now());
        postComment.setPost(post);
        postComment.setReplies(new ArrayList<>());
        commentRepository.save(postComment);
        postComment = (PostComment) commentRepository.findAll().get(0);

        mockMvc.perform(multipart("http://localhost:8080//api/comment/add")
                        .param("id", postComment.getId().toString())
                        .param("responseType", ResponseType.COMMENT.name())
                        .param("content", "reply"))
                .andExpect(status().isOk());
        Assertions.assertEquals(2, commentRepository.count());

        Comment reply = commentRepository.findAll().stream().filter(c -> c.getContents().equals("reply")).toList().get(0);
        Assertions.assertEquals("reply", reply.getContents());
        Assertions.assertEquals("user1", reply.getAuthor().getUsername());
    }

    @Test
    @Transactional
    void invalidCommentReply() throws Exception {

        PostComment postComment = new PostComment();
        postComment.setContents("content");
        postComment.setAuthor(user);
        postComment.setCreated(Instant.now());
        postComment.setPost(post);
        commentRepository.save(postComment);
        postComment = (PostComment) commentRepository.findAll().get(0);

        mockMvc.perform(multipart("http://localhost:8080//api/comment/add")
                        .param("id", postComment.getId().toString())
                        .param("responseType", ResponseType.COMMENT.name())
                        .param("content", ""))
                .andExpect(status().isBadRequest());
        Assertions.assertEquals(1, commentRepository.count());
    }

    //POST REPLY

    @Test
    @Transactional
    void postAndDelete() throws Exception {
        mockMvc.perform(multipart("http://localhost:8080//api/comment/add")
                        .param("id", post.getId().toString())
                        .param("responseType", ResponseType.POST.name())
                        .param("content", "content"))
                .andExpect(status().isOk());
        Assertions.assertEquals(1, commentRepository.count());
        Comment comment = commentRepository.findAll().get(0);
        mockMvc.perform(delete("http://localhost:8080//api/comment/" + comment.getId()))
                .andExpect(status().isOk());

        Comment deletedComment = commentRepository.findAll().get(0);
        Assertions.assertEquals("[DELETED]", comment.getContents());
        Assertions.assertNull(comment.getAuthor());
    }

    @Test
    @Transactional
    @WithMockUser("admin")
    void postAndDeleteAdmin() throws Exception {
        UserEntity admin = new UserEntity();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setEmail("admin@admin.com");

        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        roleRepository.save(adminRole);

        List<Role> roles = new ArrayList<>();
        roles.add(role);
        roles.add(adminRole);
        admin.setRoles(roles);
        userRepository.save(admin);

        mockMvc.perform(multipart("http://localhost:8080//api/comment/add")
                        .param("id", post.getId().toString())
                        .param("responseType", ResponseType.POST.name())
                        .param("content", "content"))
                .andExpect(status().isOk());
        Assertions.assertEquals(1, commentRepository.count());
        Comment comment = commentRepository.findAll().get(0);

        mockMvc.perform(delete("http://localhost:8080//api/comment/" + comment.getId()))
                .andExpect(status().isOk());

        comment = commentRepository.findAll().get(0);
        Assertions.assertEquals("[DELETED]", comment.getContents());
        Assertions.assertNull(comment.getAuthor());
    }

    @Test
    @Transactional
    @WithMockUser("user2")
    void postAndDeleteInvalid() throws Exception {
        UserEntity user2 = new UserEntity();
        user2.setUsername("user2");
        user2.setPassword(passwordEncoder.encode("user2"));
        user2.setEmail("admin@admin.com");

        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user2.setRoles(roles);

        userRepository.save(user2);

        PostComment comment = new PostComment();
        comment.setContents("content");
        comment.setAuthor(user);
        comment.setCreated(Instant.now());
        comment.setPost(post);
        commentRepository.save(comment);

        Assertions.assertEquals(1, commentRepository.count());
        Comment commentFromDB = commentRepository.findAll().get(0);
        mockMvc.perform(delete("http://localhost:8080//api/comment/" + commentFromDB.getId()))
                .andExpect(status().isForbidden());
        Assertions.assertEquals(1, commentRepository.count());

    }

    //COMMENT REPLY
    @Test
    @Transactional
    void replyAndDelete() throws Exception {

        PostComment postComment = new PostComment();
        postComment.setContents("content");
        postComment.setAuthor(user);
        postComment.setCreated(Instant.now());
        postComment.setPost(post);
        postComment.setReplies(new ArrayList<>());
        commentRepository.save(postComment);
        postComment = (PostComment) commentRepository.findAll().get(0);

        mockMvc.perform(multipart("http://localhost:8080//api/comment/add")
                        .param("id", postComment.getId().toString())
                        .param("responseType", ResponseType.COMMENT.name())
                        .param("content", "reply"))
                .andExpect(status().isOk());
        Assertions.assertEquals(2, commentRepository.count());
        Comment reply = commentRepository.findAll().stream().filter(r -> r.getContents().equals("reply")).toList().get(0);
        mockMvc.perform(delete("http://localhost:8080//api/comment/" + reply.getId()))
                .andExpect(status().isOk());

        reply = commentRepository.findAll().stream().filter(r -> r.getContents().equals("[DELETED]")).toList().get(0);
        Assertions.assertEquals("[DELETED]", reply.getContents());
        Assertions.assertNull(reply.getAuthor());
    }

    @Test
    @Transactional
    @WithMockUser("admin")
    void replyAndDeleteAdmin() throws Exception {

        PostComment postComment = new PostComment();
        postComment.setContents("content");
        postComment.setAuthor(user);
        postComment.setCreated(Instant.now());
        postComment.setPost(post);
        postComment.setReplies(new ArrayList<>());
        commentRepository.save(postComment);
        postComment = (PostComment) commentRepository.findAll().get(0);

        UserEntity admin = new UserEntity();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setEmail("admin@admin.com");

        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        roleRepository.save(adminRole);

        List<Role> roles = new ArrayList<>();
        roles.add(role);
        roles.add(adminRole);
        admin.setRoles(roles);
        userRepository.save(admin);

        mockMvc.perform(multipart("http://localhost:8080//api/comment/add")
                        .param("id", postComment.getId().toString())
                        .param("responseType", ResponseType.COMMENT.name())
                        .param("content", "reply"))
                .andExpect(status().isOk());
        Assertions.assertEquals(2, commentRepository.count());

        Comment reply = commentRepository.findAll().stream().filter(r -> r.getContents().equals("reply")).toList().get(0);
        mockMvc.perform(delete("http://localhost:8080//api/comment/" + reply.getId()))
                .andExpect(status().isOk());

        reply = commentRepository.findAll().stream().filter(r -> r.getContents().equals("[DELETED]")).toList().get(0);
        Assertions.assertEquals("[DELETED]", reply.getContents());
        Assertions.assertNull(reply.getAuthor());
    }

    @Test
    @Transactional
    @WithMockUser("user2")
    void replyAndDeleteInvalid() throws Exception {
        UserEntity user2 = new UserEntity();
        user2.setUsername("user2");
        user2.setPassword(passwordEncoder.encode("user2"));
        user2.setEmail("admin@admin.com");

        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user2.setRoles(roles);

        userRepository.save(user2);

        PostComment postComment = new PostComment();
        postComment.setContents("content");
        postComment.setAuthor(user);
        postComment.setCreated(Instant.now());
        postComment.setPost(post);

        List<Reply> replies = new ArrayList<>();
        Reply reply  = new Reply();
        reply.setAuthor(user);
        reply.setContents("reply");
        reply.setCreated(Instant.now());
        replies.add(reply);

        postComment.setContents("content");
        postComment.setAuthor(user);
        postComment.setCreated(Instant.now());
        postComment.setPost(post);
        postComment.setReplies(replies);

        commentRepository.save(postComment);
        commentRepository.save(reply);

        Assertions.assertEquals(2, commentRepository.count());
        Comment commentFromDB = commentRepository.findAll().get(0);
        mockMvc.perform(delete("http://localhost:8080//api/comment/" + commentFromDB.getId()))
                .andExpect(status().isForbidden());
        Assertions.assertEquals(2, commentRepository.count());

    }

    @BeforeEach
    void setUp() {
        getGroupEntity();
        getRole();
        getUser();
        getPost();
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

    @AfterEach
    @Transactional
    void tearDown() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        groupRepository.deleteAll();
    }
}