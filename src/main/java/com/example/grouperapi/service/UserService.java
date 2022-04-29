package com.example.grouperapi.service;

import com.example.grouperapi.model.dto.RegistrationDTO;
import com.example.grouperapi.model.entities.User;

import java.util.Optional;

public interface UserService {
    void seedUsers();

    Optional<User> registerUser(RegistrationDTO registrationDTO);

    User getUserByUsername(String username);
}
