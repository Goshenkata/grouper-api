package com.example.grouperapi.service;

import com.example.grouperapi.model.dto.*;
import com.example.grouperapi.model.entities.Image;
import com.example.grouperapi.model.entities.Role;
import com.example.grouperapi.model.entities.UserEntity;
import com.example.grouperapi.repositories.ImageRepository;
import com.example.grouperapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.BoundExtractedResult;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ImageRepository imageRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //get the user or throw an exception if not found
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> {
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
                registerUser(new RegistrationDTO("user" + i, "user@user.bg" + i, "UserUser@" + i, "user" + i, true));
            }
            UserEntity admin = new UserEntity();
            Image image = new Image();
            admin.setUsername("admin");
            admin.setEmail("admin@admin.bg");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRoles(new ArrayList<>());
            admin.getRoles().add(roleService.getUserRole());
            admin.getRoles().add(roleService.getAdminRole());
            image.setUrl("https://res.cloudinary.com/dcsi2qq6y/image/upload/v1657465724/EruW1H6WMAQsB5j_iwa27p_lxg5h7.jpg");
            image.setPublicId("EruW1H6WMAQsB5j_iwa27p_lxg5h");
            imageRepository.save(image);
            admin.setPfp(image);
            userRepository.save(admin);

            imageRepository.save(image);
            log.info("seeded users");
        } else {
            log.debug("users already initialised, moving on...");
        }
    }

    public Optional<UserEntity> registerUser(RegistrationDTO registrationDTO) {
        UserEntity user = modelMapper.map(registrationDTO, UserEntity.class);
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
        userRepository.save(user);
        return Optional.of(user);
    }

    public UserEntity getUserByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user with username: " + username + " not found"));
    }


    @GetMapping("/profile-widget")
    public ProfileWidgetDTO getProfileWidget(String name) {
        UserEntity userByUsername = this.getUserByUsername(name);
        return modelMapper.map(userByUsername, ProfileWidgetDTO.class);
    }


    @Cacheable("userSearch")
    public List<ObjectSearchReturnDTO> getUserSearch(String query) {
        return FuzzySearch
                .extractSorted(
                        query,
                        userRepository.findAll().stream().map(user -> modelMapper.map(user, ObjectSearchReturnDTO.class)).toList(),
                        ObjectSearchReturnDTO::getName,
                        4)
                .stream()
                .limit(4)
                .map(BoundExtractedResult::getReferent)
                .toList();
    }

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
    @CacheEvict(cacheNames = "userSearch", allEntries = true)
    public void refreshCache() {
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public UserInfoDTO getUserInfo(String username) {
        return modelMapper.map(getUserByUsername(username), UserInfoDTO.class);
    }

    @Transactional
    public void removePfp(String name) throws IOException {
        UserEntity user = getUserByUsername(name);
        if (user.getPfp() != null) {
            Image pfp = user.getPfp();
            cloudinaryService.deleteImage(pfp);
            imageRepository.delete(pfp);
        }
        user.setPfp(null);
        userRepository.save(user);
    }

    @Transactional
    public void changePfp(MultipartFile multipartFile, String name) throws IOException {
        UserEntity user = getUserByUsername(name);
        //todo delete
        if (user.getPfp() != null) {
            Image pfp = user.getPfp();
            cloudinaryService.deleteImage(pfp);
            imageRepository.delete(pfp);
        }
        Image image = cloudinaryService.postImage(multipartFile);
        imageRepository.save(image);
        user.setPfp(image);
        userRepository.save(user);
    }

    public void changeDescription(String description, String name) {
        UserEntity user = getUserByUsername(name);
        user.setDescription(description);
        userRepository.save(user);
    }

    public boolean isAdmin(String name) {
        return getUserByUsername(name).getRoles().stream().map(Role::getName).toList().contains("ROLE_ADMIN");
    }

    public RolesDTO getRolesArr(String username) {
        String[] roles = this.loadUserByUsername(username)
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
        return new RolesDTO(roles);
    }

    public void makeAdmin(String username) {
        UserEntity user = getUserByUsername(username);
        if (!user.getRoles().contains(roleService.getAdminRole())) {
            user.getRoles().add(roleService.getAdminRole());
        }
        userRepository.save(user);
    }
}
