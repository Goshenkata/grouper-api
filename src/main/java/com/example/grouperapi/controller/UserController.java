package com.example.grouperapi.controller;

import static com.example.grouperapi.filter.CustomAuthenticationFilter.ACCESS_TOKEN_EXPRATION;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.grouperapi.model.dto.DescriptionDTO;
import com.example.grouperapi.model.dto.ProfileWidgetDTO;
import com.example.grouperapi.model.dto.RegistrationDTO;
import com.example.grouperapi.model.dto.RolesDTO;
import com.example.grouperapi.model.dto.UserInfoDTO;
import com.example.grouperapi.model.entities.Role;
import com.example.grouperapi.model.entities.UserEntity;
import com.example.grouperapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for handling action related to users and refreshing JWT tokens
 */
@RestController
@RequestMapping("/api/user/")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegistrationDTO registrationDTO,
            BindingResult bindingResult) {

        // check if any of the fields are invalid, fronend validation for this
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        Optional<UserEntity> userOptional = userService.registerUser(registrationDTO);

        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("username or email are already taken");
        }

        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/user/register")
                .toUriString());
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("verysecretbtw".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                UserEntity user = userService.getUserByUsername(username);
                Date exp = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPRATION);
                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(exp)
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",
                                user.getRoles().stream().map(Role::getName).toList())
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);
                tokens.put("expires_at", String.valueOf(exp.getTime()));
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception e) {
                log.error("Error logging in {}", e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("refresh token is missing");
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("profile-widget")
    public ResponseEntity<ProfileWidgetDTO> profileWidgetDTO(Principal principal) {
        return ResponseEntity.ok(userService.getProfileWidget(principal.getName()));
    }

    @GetMapping("{username}")
    public ResponseEntity<UserInfoDTO> getUserInfo(@PathVariable String username) {
        if (!userService.existsByUsername(username)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userService.getUserInfo(username));
    }

    @PatchMapping("pfp")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity pfp(@RequestParam MultipartFile pfp,
            Principal principal) throws IOException {
        userService.changePfp(pfp, principal.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("pfp")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity pfp(Principal principal) throws IOException {
        userService.removePfp(principal.getName());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("description")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity description(@RequestBody DescriptionDTO description,
            Principal principal) throws IOException {
        userService.changeDescription(description.getDescription(), principal.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("roles")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RolesDTO> roles(Principal principal) {
        return ResponseEntity.ok(userService.getRolesArr(principal.getName()));
    }

    @GetMapping("roles/{username}")
    public ResponseEntity<RolesDTO> roles(@PathVariable String username) {
        return ResponseEntity.ok(userService.getRolesArr(username));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("roles/admin/{username}")
    public ResponseEntity makeAdmin(@PathVariable String username) {
            userService.makeAdmin(username);
        return ResponseEntity.ok().build();
    }


}
