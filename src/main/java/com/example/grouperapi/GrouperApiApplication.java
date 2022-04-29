package com.example.grouperapi;

import com.example.grouperapi.service.RoleService;
import com.example.grouperapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class GrouperApiApplication implements CommandLineRunner {
	private final UserService userService;
	private final RoleService roleService;

	public static void main(String[] args) {
		SpringApplication.run(GrouperApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		roleService.seedRoles();
		userService.seedUsers();
	}
}
