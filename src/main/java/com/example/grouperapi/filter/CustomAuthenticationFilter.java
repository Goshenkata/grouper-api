package com.example.grouperapi.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    //6 month expiration, after which new login is required
    public static final long REFRESH_TOKEN_EXPIRATION = 15778800000L;

    //1 week expiration, after which the token is refreshed
    public static final long ACCESS_TOKEN_EXPRATION = 604800000L;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256("verysecretbtw".getBytes());
        Date accessTokenExpiration = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPRATION);
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                //1 minute expiration
                .withExpiresAt(accessTokenExpiration)
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles",
                        user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .sign(algorithm);
        Date refreshTokenExpiration = new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION);
        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withIssuer(request.getRequestURL().toString())
                //2 minutes expiration
                .withExpiresAt(refreshTokenExpiration)
                .withClaim("roles",
                        user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .sign(algorithm);
        Map<String, Object> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        tokens.put("expires_at", accessTokenExpiration);
        tokens.put("username", request.getParameter("username"));
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
    //todo override unsuccessfulAuthentication to check number of requests per user and stop brute force attacks
}
