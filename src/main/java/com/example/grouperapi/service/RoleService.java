package com.example.grouperapi.service;

import com.example.grouperapi.model.entities.Role;
import com.example.grouperapi.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {
    private final RoleRepository roleRepository;

    public void seedRoles() {
        if (roleRepository.count() == 0) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);

            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);

            log.info("roles initialised");
        } else {
            log.debug("roles already initialized, moving on...");
        }
    }

    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("role ROLE_USER not found in the database"));
    }

    public Role getAdminRole() {
        return roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("role ROLE_ADMIN not found in the database"));
    }
}
