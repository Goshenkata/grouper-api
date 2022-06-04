package com.example.grouperapi.service.impl;

import com.example.grouperapi.model.entities.GroupEntity;
import com.example.grouperapi.model.entities.User;
import com.example.grouperapi.repositories.GroupRepository;
import com.example.grouperapi.repositories.UserRepository;
import com.example.grouperapi.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;@Override
    public void seedGroups() {
        List<User> members = List.of(
                userRepository.findByUsername("user").orElseThrow(() -> new UsernameNotFoundException("default user \"user\" not found")),
                userRepository.findByUsername("user").orElseThrow(() -> new UsernameNotFoundException("default user \"user\" not found"))
        );
        createGroup("bulgaria", members);
        createGroup("linux", members);
        createGroup("programming", members);
        createGroup("politics", members);
        createGroup("news", members);
        createGroup("europe", members);
        createGroup("conversation", members);
        createGroup("random", members);
        createGroup("memes", members);
        createGroup("cats", members);
    }


    private void createGroup(String name, List<User> members) {
        GroupEntity entity = new GroupEntity();
        entity.setName(name);
        entity.setMembers(members);
        entity.setPosts(new ArrayList<>());
        groupRepository.save(entity);
    }
}
