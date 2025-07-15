package com.project.bookahikeservice.service.impl;

import com.project.bookahikeservice.controller.UserController;
import com.project.bookahikeservice.entity.User;
import com.project.bookahikeservice.repository.UserRepository;
import com.project.bookahikeservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private  UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        logger.info("POST /api/user/create-user called");
        logger.info("User {} has been added", user);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        logger.info("Get /api/user/{id} called");
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

