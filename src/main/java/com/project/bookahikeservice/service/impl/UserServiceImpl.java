package com.project.bookahikeservice.service.impl;

import com.project.bookahikeservice.controller.UserController;
import com.project.bookahikeservice.dto.request.UserRegistrationDto;
import com.project.bookahikeservice.entity.Role;
import com.project.bookahikeservice.entity.User;
import com.project.bookahikeservice.repository.RoleRepository;
import com.project.bookahikeservice.repository.UserRepository;
import com.project.bookahikeservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(UserRegistrationDto dto) {

        logger.info("Registering user with email: {}", dto.getEmail());
        // Check if user already exists
        userRepository.findByEmail(dto.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Email is already taken");
        });

        // Encrypt the password
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // Get the default role
        Role joinerRole = roleRepository.findByName("ROLE_JOINER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        // Map DTO to User
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setNumber(dto.getNumber());
        user.setPassword(encodedPassword);
        user.setRoles(Collections.singleton(joinerRole));

        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        logger.info("GET /api/user/{} called", id);
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        logger.info("GET /api/user called");
        logger.info("Fetched {} users from the database", users.size());
        return users;
    }
}
