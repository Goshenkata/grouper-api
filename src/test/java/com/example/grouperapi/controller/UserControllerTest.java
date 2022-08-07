package com.example.grouperapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.example.grouperapi.init.DBInit;
import com.example.grouperapi.model.dto.DescriptionDTO;
import com.example.grouperapi.model.dto.RegistrationDTO;
import com.example.grouperapi.model.entities.Role;
import com.example.grouperapi.model.entities.UserEntity;
import com.example.grouperapi.repositories.RoleRepository;
import com.example.grouperapi.repositories.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.aspectj.weaver.patterns.IfPointcut.IfFalsePointcut;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class UserControllerTest {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @MockBean
    DBInit dbInit;
    @Autowired
    MockMvc mockMvc;
    private Role role;
    private UserEntity user;

    @Test
    void registerSuccess() throws Exception {
        Gson gson = new Gson();
        RegistrationDTO registrationDTO = new RegistrationDTO("User1@User", "User1@User", "User1@User", "User1@User",
                true);
        mockMvc.perform(post("http://localhost:8080/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(registrationDTO)))
                .andExpect(status().isCreated());
        Assertions.assertEquals(1, userRepository.count());
        UserEntity userEntity = userRepository.findAll().get(0);
        Assertions.assertEquals(1, userRepository.count());
        Assertions.assertEquals(userEntity.getUsername(), "User1@User");
    }

    @Test
    void registerFail() throws Exception {
        Gson gson = new Gson();
        RegistrationDTO registrationDTO = new RegistrationDTO("User1@User", "User1@User", "User1@User", "User1@User",
                false);
        mockMvc.perform(post("http://localhost:8080/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(registrationDTO)))
                .andExpect(status().isBadRequest());
        Assertions.assertEquals(0, userRepository.count());
    }

    @Test
    @WithMockUser("user1")
    void loginAndRefreshToken() throws Exception {
        getUser();

        mockMvc.perform(multipart("http://localhost:8080/login")
                .param("username", "user1")
                .param("password", "UserUser@1"))
                .andExpect(jsonPath("$.access_token").isString())
                .andExpect(jsonPath("$.refresh_token").isString())
                .andExpect(jsonPath("$.expires_at").exists())
                .andDo(result -> {
                    String jsonString = result.getResponse().getContentAsString();
                    JsonObject obj = JsonParser.parseString(jsonString).getAsJsonObject();
                    String refreshToken = obj.get("refresh_token").getAsString();
                    log.info(refreshToken);

                    mockMvc.perform(get("http://localhost:8080/api/user/refreshToken")
                            .header("authorization", "Bearer " + refreshToken))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.access_token").isString())
                            .andExpect(jsonPath("$.refresh_token").isString())
                            .andExpect(jsonPath("$.expires_at").exists());
                });
    }

    @Test
    @WithMockUser("user1")
    void profileWidget() throws Exception {
        getUser();

        mockMvc.perform(get("http://localhost:8080/api/user/profile-widget"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.pfpUrl").doesNotExist());
    }

    @Test
    void profileWidgetInvalid() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/user/profile-widget"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUserInfo() throws Exception {
        getUser();

        mockMvc.perform(get("http://localhost:8080/api/user/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imageUrl").doesNotExist())
                .andExpect(jsonPath("$.name", is(user.getUsername())))
                .andExpect(jsonPath("$.description", is(user.getDescription())));
    }

    @Test
    @WithMockUser("user1")
    void changeDescription() throws Exception {
        getUser();

        Gson gson = new Gson();
        DescriptionDTO description = new DescriptionDTO("new description");

        mockMvc.perform(patch("http://localhost:8080/api/user/description")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(description)))
                .andExpect(status().isOk());
        UserEntity userEntity = userRepository.findAll().get(0);
        Assertions.assertEquals("new description", userEntity.getDescription());
    }

    @Test
    void getRoles() throws Exception {
        getUser();

        mockMvc.perform(get("http://localhost:8080/api/user/roles/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roles[0]", is("ROLE_USER")));
    }

    @Test
    @WithMockUser("user1")
    void getRoleOfPrincipal() throws Exception {
        getUser();

        mockMvc.perform(get("http://localhost:8080/api/user/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roles[0]", is("ROLE_USER")));
    }

    @Test
    @WithMockUser("admin")
    @Transactional
    void makeAdmin() throws Exception {
        getUser();
        getAdmin();

        mockMvc.perform(multipart("http://localhost:8080/login")
                .param("username", "admin")
                .param("password", "admin"))
                .andExpect(jsonPath("$.access_token").isString())
                .andExpect(jsonPath("$.refresh_token").isString())
                .andExpect(jsonPath("$.expires_at").exists())
                .andDo(result -> {
                    String jsonString = result.getResponse().getContentAsString();
                    JsonObject obj = JsonParser.parseString(jsonString).getAsJsonObject();
                    String accessToken = obj.get("access_token").getAsString();
                    mockMvc.perform(patch("http://localhost:8080/api/user/roles/admin/user1")
                            .header("authorization", "Bearer " + accessToken))
                            .andExpect(status().isOk());
                });

        UserEntity userAfter = userRepository
                .findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("user1 not found in db"));
        Assertions.assertTrue(
                userAfter.getRoles().stream()
                        .anyMatch(r -> r.getName().equals("ROLE_ADMIN")));
    }

    @Test
    @WithMockUser("user1")
    void makeAdminForbidden() throws Exception {
        getUser();
        mockMvc.perform(patch("http://localhost:8080/api/user/roles/admin/user"))
                .andExpect(status().isForbidden());
    }

    private UserEntity getAdmin() {
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

        return admin;
    }

    private Role getRole() {
        Role role = new Role();
        role.setName("ROLE_USER");
        roleRepository.save(role);
        this.role = role;
        return role;
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

    @BeforeEach
    public void setUp() {
        getRole();
    }

    @AfterEach
    @Transactional
    public void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

}
