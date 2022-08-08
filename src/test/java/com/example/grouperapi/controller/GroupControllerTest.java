package com.example.grouperapi.controller;

import com.example.grouperapi.init.DBInit;
import com.example.grouperapi.model.entities.GroupEntity;
import com.example.grouperapi.repositories.GroupRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class GroupControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DBInit dbInit;
    @Autowired
    private GroupRepository groupRepository;
    private GroupEntity group;


    @Test
    void getGroup() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/group/dogs"))
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(group.getName())))
                .andExpect(jsonPath("$.iconUrl", is(group.getIconUrl())) )
                .andExpect(jsonPath("$.description", is(group.getDescription())));

    }

    @Test
    void getNonExistentGroup() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/group/linux"))
                .andExpect(status().isNotFound());
    }


    @BeforeEach
    void setUp() {
        GroupEntity group = new GroupEntity();
        group.setName("dogs");
        group.setIconUrl("https://clubk9.com/2018/07/ask-to-pet/");
        group.setDescription("dogs are cute");
        this.group = group;
        groupRepository.save(group);
    }

    @AfterEach
    void tearDown() {
        groupRepository.deleteAll();
    }

}