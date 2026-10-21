package com.schoollibrary.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoollibrary.User;
import com.schoollibrary.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody Map<String, String> request) {

        try {
            User user = authService.registerUser(
                    request.get("username"),
                    request.get("password"));

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "User registered successfully.",
                            "username", user.getUsername(),
                            "role", user.getRole()));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", exception.getMessage()));
        }
    }

@PostMapping("/login")
public ResponseEntity<?> login(
        @RequestBody Map<String, String> request,
        jakarta.servlet.http.HttpServletRequest httpRequest) {

    try {
        User user = authService.authenticate(
                request.get("username"),
                request.get("password"));

        var authentication =
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        null,
                        java.util.List.of(
                                new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                        "ROLE_" + user.getRole()
                                )
                        )
                );

        var securityContext =
                org.springframework.security.core.context.SecurityContextHolder
                        .createEmptyContext();

        securityContext.setAuthentication(authentication);

        org.springframework.security.core.context.SecurityContextHolder
                .setContext(securityContext);

        httpRequest.getSession(true).setAttribute(
                org.springframework.security.web.context.HttpSessionSecurityContextRepository
                        .SPRING_SECURITY_CONTEXT_KEY,
                securityContext
        );

        return ResponseEntity.ok(Map.of(
                "message", "Login successful.",
                "username", user.getUsername(),
                "role", user.getRole()
        ));

    } catch (IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "message", "Invalid username or password."
                ));
    }
        }
  
}
