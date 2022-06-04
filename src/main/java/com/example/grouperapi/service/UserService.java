package com.example.grouperapi.service;

import com.example.grouperapi.model.dto.RegistrationDTO;
import com.example.grouperapi.model.entities.User;

import java.util.Optional;

public interface UserService {
    void seedUsers();


    /**
     * @return the newly registered user, or empty if the username is taken
     */
    Optional<User> registerUser(RegistrationDTO registrationDTO);

    User getUserByUsername(String username);

}
