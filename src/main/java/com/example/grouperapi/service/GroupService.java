package com.example.grouperapi.service;

import com.example.grouperapi.model.dto.ObjectSearchReturnDTO;
import com.example.grouperapi.model.entities.GroupEntity;
import com.example.grouperapi.model.entities.Image;
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
            createGroup("bulgaria", members,"https://res.cloudinary.com/dcsi2qq6y/image/upload/v1658668423/bulgaria-53-658916_ixzbgx.png");
            createGroup("linux", members, "https://res.cloudinary.com/dcsi2qq6y/image/upload/v1658668473/tux2_aup8ck.ico");
            createGroup("programming",members, "https://res.cloudinary.com/dcsi2qq6y/image/upload/v1658668508/IntroToProgrammingImage_Python_iid1gh.png");
            createGroup("politics", members, null);
            createGroup("news", members,null);
            createGroup("europe", members, "https://res.cloudinary.com/dcsi2qq6y/image/upload/v1658668663/icon-256x256_rv9rxa.png");
            createGroup("conversation", members, null);
            createGroup("random", members, null);
            createGroup("memes", members, null);
            createGroup("cats", members, "https://res.cloudinary.com/dcsi2qq6y/image/upload/v1658668600/1be494c3c065c9c97da3231f7303ee85_f6jtr6.jpg");
        }
    }

    public GroupEntity getGroupByName(String name) {
        return groupRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("Group with name \"" + name + "\" not found in the database"));
    }

    public Optional<GroupEntity> findByGroupByName(String name) {
        return groupRepository.findByName(name);
    }


    private void createGroup(String name, List<User> members, String iconUrl) {
        GroupEntity entity = new GroupEntity();
        entity.setName(name);
        entity.setMembers(members);
        entity.setPosts(new ArrayList<>());
        entity.setIconUrl(iconUrl);
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
    }
}