package com.example.grouperapi.init;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import com.example.grouperapi.repositories.CommentRepository;
import com.example.grouperapi.repositories.GroupRepository;
import com.example.grouperapi.repositories.ImageRepository;
import com.example.grouperapi.repositories.PostRepository;
import com.example.grouperapi.repositories.RoleRepository;
import com.example.grouperapi.repositories.UserRepository;

import org.assertj.core.api.Fail;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * DbInit
 */
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class DbInitTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    RoleRepository roleRepository;

    @Test
    @Transactional
    void dbFull() throws Exception {
        Assertions.assertTrue(userRepository.count() > 0);
        Assertions.assertTrue(commentRepository.count() > 0);
        Assertions.assertTrue(postRepository.count() > 0);
        Assertions.assertTrue(groupRepository.count() > 0);
        Assertions.assertTrue(roleRepository.count() > 0);
    }

}
