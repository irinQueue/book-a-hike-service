package com.example.bookahikeservice.controller;


import com.example.bookahikeservice.entity.User;
import com.example.bookahikeservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @GetMapping("/api/user")
    public List<User> getUser() {
        logger.info("GET /api/user called");
        List<User> users = userService.getUser();
        logger.info("Retrieved {} users", users);
        return users;
    }
}
