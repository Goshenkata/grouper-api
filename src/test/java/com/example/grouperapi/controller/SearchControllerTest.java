package com.example.grouperapi.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.example.grouperapi.init.DBInit;
import com.example.grouperapi.model.entities.GroupEntity;
import com.example.grouperapi.model.entities.Role;
import com.example.grouperapi.model.entities.UserEntity;
import com.example.grouperapi.repositories.GroupRepository;
import com.example.grouperapi.repositories.RoleRepository;
import com.example.grouperapi.repositories.UserRepository;

import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


/**
 * SearchControllerTest
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SearchControllerTest {
    @Autowired
    PasswordEncoder passwordEncoder;
    @MockBean
    DBInit dbInit;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
	private GroupEntity group;
	private Role role;
	private UserEntity user;
    @Autowired
    MockMvc mockMvc;

    @Test
    void searchGroup() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/get-object?query=d&objectType=GROUP"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].imageUrl").doesNotExist())
            .andExpect(jsonPath("$[0].name", is("dogs")));
    }

    @Test
    void searchUser() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/get-object?query=u&objectType=USER"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].imageUrl").doesNotExist())
            .andExpect(jsonPath("$[0].name", is("user1")));
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
        user.setPfp(null);
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

    @BeforeEach
    void setUp() {
        getRole();
        getGroupEntity();
        getUser();
    }

    @AfterEach
    void tearDown() {
        groupRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

}
