package com.project.bookahikeservice.service.impl;

import com.project.bookahikeservice.controller.UserController;
import com.project.bookahikeservice.entity.User;
import com.project.bookahikeservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.bookahikeservice.repository.UserRepository;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private  UserRepository userRepository;

    @Override
    public List<User> getUser() {
        logger.info("GET /api/user called");
        List<User> users = userRepository.findAll();
        logger.info("Fetched {} users from the database", users.size());
        return users;
    }
}

