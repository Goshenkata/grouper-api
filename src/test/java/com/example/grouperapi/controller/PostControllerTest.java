package com.example.grouperapi.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import ch.qos.logback.core.net.ObjectWriter;
import com.example.grouperapi.init.DBInit;
import com.example.grouperapi.model.dto.ImageDTO;
import com.example.grouperapi.model.dto.PostCreationDTO;
import com.example.grouperapi.model.entities.*;
import com.example.grouperapi.model.entities.enums.PostType;
import com.example.grouperapi.repositories.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.GsonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser("user1")
public class PostControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ImageRepository imageRepository;
    @MockBean
    private DBInit dbInit;
    private Post post;
    private UserEntity user;
    private Role role;
    private GroupEntity group;
    @Autowired
    private ModelMapper mapper;
    private Image image;

    @Test
    void getFullPostInfo() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/post/" + 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.postType", is(PostType.IMAGE.name())))
                .andExpect(jsonPath("$.postAuthor.pfp.url", is(user.getPfp().getUrl())))
                .andExpect(jsonPath("$.postAuthor.username", is(user.getUsername())))
                .andExpect(jsonPath("$.postAuthor.roles[0].name", is(role.getName())))
                .andExpect(jsonPath("$.content", is(post.getContent())))
                .andExpect(jsonPath("$.title", is(post.getTitle())))
                .andExpect(jsonPath("$.group.name", is(group.getName())))
                .andExpect(jsonPath("$.group.iconUrl", is(group.getIconUrl())))
                .andExpect(jsonPath("$.image.url", is(image.getUrl())))
                .andExpect(jsonPath("$.image.publicId", is(image.getPublicId())))
                .andExpect(jsonPath("$.commentCount", is(0)));
    }

    @Test
    void getWrongPostInfo() throws Exception {
        tearDown();
        mockMvc.perform(get("http://localhost:8080/api/post/" + 2))
                .andExpect(status().isNotFound());
    }

    @Test
    void createPost() throws Exception {
        mockMvc.perform(
                multipart("http://localhost:8080/api/post")
                        .file(new MockMultipartFile("image", new byte[1]))
                        .param("title", "title")
                        .param("content", "content")
                        .param("groupName", "dogs")
                        )
                .andExpect(status().isCreated());
    }

    @BeforeEach
    void setUp() {
        //IMAGE
        Image image = new Image();
        image.setUrl("http://res.cloudinary.com/dcsi2qq6y/image/upload/v1659273509/czzvmcokar8egqtpdipj.jpg");
        image.setPublicId("czzvmcokar8egqtpdipj");
        imageRepository.save(image);
        this.image = image;

        //GROUP
        GroupEntity group = new GroupEntity();
        group.setName("dogs");
        group.setIconUrl("https://clubk9.com/2018/07/ask-to-pet/");
        group.setDescription("dogs are cute");
        groupRepository.save(group);
        this.group = group;

        //ROLE
        Role role = new Role();
        role.setName("ROLE_USER");
        roleRepository.save(role);
        this.role = role;

        //USER
        UserEntity user = new UserEntity();
        user.setUsername("user1");
        user.setPassword(passwordEncoder.encode("UserUser@1"));
        user.setEmail("user@user1");
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);
        user.setPfp(image);
        userRepository.save(user);
        this.user = user;

        //POST
        Post post = new Post();
        post.setAuthor(user);
        post.setPostType(PostType.IMAGE);
        post.setCreated(Instant.now());
        post.setGroup(group);
        post.setTitle("dogs are cute");
        post.setContent(":D");
        post.setImage(image);
        postRepository.save(post);
        this.post = post;
    }

    @AfterEach
    void tearDown() {
        postRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        groupRepository.deleteAll();
        imageRepository.deleteAll();
    }
}