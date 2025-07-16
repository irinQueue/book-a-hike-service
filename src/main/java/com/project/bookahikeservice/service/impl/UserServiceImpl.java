package com.project.bookahikeservice.service.impl;

import com.project.bookahikeservice.controller.UserController;
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
    public User saveUser(User user) {
        logger.info("POST /api/user/create-user called");

        // Encrypt the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assign default role: ROLE_JOINER
        Role joinerRole = roleRepository.findByName("ROLE_JOINER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRoles(Collections.singleton(joinerRole));

        logger.info("User {} has been added with role {}", user.getEmail(), joinerRole.getName());
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
