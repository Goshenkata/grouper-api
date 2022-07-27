package com.example.grouperapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class GrouperApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrouperApiApplication.class, args);
	}

}
