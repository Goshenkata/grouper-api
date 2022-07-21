package com.example.grouperapi;

import com.example.grouperapi.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@EnableCaching
@EnableScheduling
@SpringBootApplication
@RequiredArgsConstructor
public class GrouperApiApplication implements CommandLineRunner {
	private final GroupService groupService;
	private final UserService userService;
	private final RoleService roleService;
	private final PostService postService;
	private final CommentService commentService;

	public static void main(String[] args) {
		SpringApplication.run(GrouperApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		roleService.seedRoles();
		userService.seedUsers();
		groupService.seedGroups();
		postService.seedPosts();
		commentService.seedComments();
	}
}
