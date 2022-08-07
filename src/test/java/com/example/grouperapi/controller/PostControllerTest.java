package com.example.grouperapi.controller;

import com.example.grouperapi.init.DBInit;
import com.example.grouperapi.model.entities.*;
import com.example.grouperapi.model.entities.enums.PostType;
import com.example.grouperapi.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        mockMvc.perform(get("http://localhost:8080/api/post/" + post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(post.getId().intValue())))
                .andExpect(jsonPath("$.postType", is(PostType.IMAGE.name())))
                .andExpect(jsonPath("$.postAuthor.pfp.url", is(user.getPfp().getUrl())))
                .andExpect(jsonPath("$.postAuthor.username", is(user.getUsername())))
                .andExpect(jsonPath("$.postAuthor.roles[0].name", is(role.getName())))
                .andExpect(jsonPath("$.content", is(post.getContent())))
                .andExpect(jsonPath("$.title", is(post.getTitle())))
                .andExpect(jsonPath("$.group.name", is(group.getName())))
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
        postRepository.deleteAll();
        mockMvc.perform(
                        multipart("http://localhost:8080/api/post/")
                                .param("title", "title")
                                .param("content", "content")
                                .param("groupName", group.getName())
                )
                .andExpect(status().isCreated());
        Post post2 = postRepository.findAll().get(0);
        Assertions.assertEquals(postRepository.count(), 1);
        Assertions.assertEquals("title", post2.getTitle());
        Assertions.assertEquals("content", post2.getContent());
        Assertions.assertEquals("dogs", group.getName());
        Assertions.assertEquals(group.getName(), post2.getGroup().getName());
    }

    @Test
    void createInvalidPost() throws Exception {
        postRepository.deleteAll();
        mockMvc.perform(
                        multipart("http://localhost:8080/api/post/")
                                .param("title", "title")
                                .param("content", "content")
                                .param("groupName", group.getName() + "nope")
                )
                .andExpect(status().isBadRequest());
        mockMvc.perform(
                multipart("http://localhost:8080/api/post/")
                        .param("title", "")
                        .param("content", "content")
                        .param("groupName", group.getName() + "nope")
        );

        mockMvc.perform(
                        multipart("http://localhost:8080/api/post/")
                                .param("title", "")
                                .param("content", "")
                                .param("groupName", group.getName() + "nope")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletePost() throws Exception {
        post.setImage(null);
        postRepository.save(post);
        mockMvc.perform(delete("http://localhost:8080/api/post/" + post.getId()))
                .andExpect(status().isOk());
        Assertions.assertEquals(0L, postRepository.count());
    }

    @Test
    @WithMockUser("admin")
    void adminDeletePost() throws Exception {
        post.setImage(null);
        postRepository.save(post);

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

        mockMvc.perform(delete("http://localhost:8080/api/post/" + post.getId()))
                .andExpect(status().isOk());
        Assertions.assertEquals(0L, postRepository.count());
    }



    @BeforeEach
    void setUp() {
        //IMAGE
        Image image = getImage();

        //GROUP
        GroupEntity group = getGroupEntity();

        //ROLE
        Role role = getRole();

        //USER
        UserEntity user = getUser(image, role);

        //POST
        getPost(image, group, user);
    }

    private Post getPost(Image image, GroupEntity group, UserEntity user) {
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
        return post;
    }

    private UserEntity getUser(Image image, Role role) {
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
        return user;
    }

    private Role getRole() {
        Role role = new Role();
        role.setName("ROLE_USER");
        roleRepository.save(role);
        this.role = role;
        return role;
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

    private Image getImage() {
        Image image = new Image();
        image.setUrl("http://res.cloudinary.com/dcsi2qq6y/image/upload/v1659273509/czzvmcokar8egqtpdipj.jpg");
        image.setPublicId("czzvmcokar8egqtpdipj");
        imageRepository.save(image);
        this.image = image;
        return image;
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
