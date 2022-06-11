package com.example.grouperapi.service.impl;

import com.example.grouperapi.model.entities.GroupEntity;
import com.example.grouperapi.model.entities.User;
import com.example.grouperapi.repositories.GroupRepository;
import com.example.grouperapi.repositories.UserRepository;
import com.example.grouperapi.service.GroupService;
import com.example.grouperapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserService userService;

    @Override
    public void seedGroups() {
        if (groupRepository.count() == 0) {
            List<User> members = List.of(
                    userService.getUserByUsername("user"),
                    userService.getUserByUsername("user2")
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
    }

    @Override
    public GroupEntity getGroupByName(String name) {
        return groupRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("Group with name \"" + name + "\" not found in the database"));
    }


    private void createGroup(String name, List<User> members) {
        GroupEntity entity = new GroupEntity();
        entity.setName(name);
        entity.setMembers(members);
        entity.setPosts(new ArrayList<>());
        groupRepository.save(entity);
    }
}
