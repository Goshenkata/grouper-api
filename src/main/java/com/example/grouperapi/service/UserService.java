package com.example.grouperapi.service;

import com.example.grouperapi.model.dto.RegistrationDTO;
import com.example.grouperapi.model.entities.User;
import com.example.grouperapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //get the user or throw an exception if not found
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("user {} not found in the database", username);
            throw new UsernameNotFoundException("user not found in the database");
        });

        //map the roles of the user
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public void seedUsers() {
        if (userRepository.count() == 0) {
            registerUser(new RegistrationDTO("user", "user@user.bg", "user", "user", true));
            for (int i = 0; i < 10; i++) {
                registerUser(new RegistrationDTO("user" + i, "user@user.bg" + i, "user" + i, "user" + i, true));
            }
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@admin.bg");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(new ArrayList<>());
            admin.getRoles().add(roleService.getUserRole());
            admin.getRoles().add(roleService.getAdminRole());
            userRepository.save(admin);
            log.info("seeded users");
        } else {
            log.debug("users already initialised, moving on...");
        }
    }

    public Optional<User> registerUser(RegistrationDTO registrationDTO) {
        User user = modelMapper.map(registrationDTO, User.class);
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        if (userRepository.existsByUsername(user.getUsername())) {
            log.warn("user {} already exists", user.getUsername());
            return Optional.empty();
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("user {} already exists", user.getUsername());
            return Optional.empty();
        }
        user.setRoles(new ArrayList<>());
        user.getRoles().add(roleService.getUserRole());
        log.info("user {} successfully registered", user.getUsername());
        userRepository.save(user);
        return Optional.of(user);
    }

    public User getUserByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user with username: " + username + " not found"));
    }


}
