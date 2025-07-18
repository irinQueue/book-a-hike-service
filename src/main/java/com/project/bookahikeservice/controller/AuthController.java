package com.project.bookahikeservice.controller;

import com.project.bookahikeservice.dto.request.LoginRequest;
import com.project.bookahikeservice.dto.response.LoginResponse;
import com.project.bookahikeservice.entity.Role;
import com.project.bookahikeservice.entity.User;
import com.project.bookahikeservice.repository.UserRepository;
import com.project.bookahikeservice.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);

        // Get roles as strings
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        LoginResponse response = new LoginResponse(
                token,
                user.getEmail(),
                user.getFirstName() + " " + user.getLastName(),
                roleNames
        );

        return ResponseEntity.ok(response);
    }
}
