package com.example.grouperapi.service;

import com.example.grouperapi.model.dto.ObjectSearchReturnDTO;
import com.example.grouperapi.model.entities.GroupEntity;
import com.example.grouperapi.model.entities.User;
import com.example.grouperapi.repositories.GroupRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.BoundExtractedResult;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Slf4j
@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserService userService;

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

    public GroupEntity getGroupByName(String name) {
        return groupRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("Group with name \"" + name + "\" not found in the database"));
    }

    public Optional<GroupEntity> findByGroupByName(String name) {
        return groupRepository.findByName(name);
    }


    private void createGroup(String name, List<User> members) {
        GroupEntity entity = new GroupEntity();
        entity.setName(name);
        entity.setMembers(members);
        entity.setPosts(new ArrayList<>());
        groupRepository.save(entity);
    }

    @Cacheable("groupSearch")
    public List<ObjectSearchReturnDTO> getGroupSearch(String query) {
        return FuzzySearch
                .extractSorted(
                        query,
                        groupRepository.getQueryResult(query),
                        ObjectSearchReturnDTO::getName,
                        50)
                .stream()
                .limit(4)
                .map(BoundExtractedResult::getReferent)
                .toList();
    }

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
    @CacheEvict(cacheNames="groupSearch", allEntries=true)
    public void refreshCache() {
        log.info("refreshed groupSearch cache");
    }
}