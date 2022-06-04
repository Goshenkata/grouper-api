package com.example.grouperapi.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.grouperapi.model.dto.RegistrationDTO;
import com.example.grouperapi.model.entities.Role;
import com.example.grouperapi.model.entities.User;
import com.example.grouperapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.*;

import static com.example.grouperapi.filter.CustomAuthenticationFilter.ACCESS_TOKEN_EXPRATION;
import static com.example.grouperapi.filter.CustomAuthenticationFilter.REFRESH_TOKEN_EXPIRATION;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
        //check if the username is taken, and if not register the new user
        Optional<User> userOptional = userService.registerUser(registrationDTO);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("username or email are already taken");
        }

        //check if any of the fields are invalid, fronend validation for this
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("bad input");
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
                User user = userService.getUserByUsername(username);
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

    @GetMapping("test")
    public ResponseEntity<String> test(Principal principal) {
        log.info("Principal name: " + principal.getName());
        return ResponseEntity.ok().body("test success");
    }
}
