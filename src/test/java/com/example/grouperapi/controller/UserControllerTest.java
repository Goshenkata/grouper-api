package com.example.grouperapi.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.example.grouperapi.init.DBInit;
import com.example.grouperapi.model.dto.RegistrationDTO;
import com.example.grouperapi.model.entities.Role;
import com.example.grouperapi.model.entities.UserEntity;
import com.example.grouperapi.repositories.RoleRepository;
import com.example.grouperapi.repositories.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
        Gson gson = new Gson();

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("user1");
        userEntity.setPfp(null);
        userEntity.setEmail("User1@User1");
        userEntity.setPassword(passwordEncoder.encode("User@User1"));

        List<Role> roles = new ArrayList<>();
        roles.add(role);
        userEntity.setRoles(roles);
        userRepository.save(userEntity);

        mockMvc.perform(multipart("http://localhost:8080/login")
                .param("username", "user1")
                .param("password", "User@User1"))
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
                            .andExpect(jsonPath("$.refresh_token").exists())
                            .andExpect(jsonPath("$.expires_at").exists());
                });

    }

	@BeforeEach
    public void setUp() {
        getRole();
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    private Role getRole() {
        Role role = new Role();
        role.setName("ROLE_USER");
        roleRepository.save(role);
        this.role = role;
        return role;
    }

}
