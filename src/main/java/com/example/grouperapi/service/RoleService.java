package com.example.grouperapi.service;

import com.example.grouperapi.model.entities.Role;

public interface RoleService {
    void  seedRoles();
    Role getUserRole();
    Role getAdminRole();
}
