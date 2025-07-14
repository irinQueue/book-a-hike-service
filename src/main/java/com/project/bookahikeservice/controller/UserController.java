package com.project.bookahikeservice.controller;


import com.project.bookahikeservice.entity.User;
import com.project.bookahikeservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping()
    public List<User> getUser() {
        return userService.getUser();
    }
    @PostMapping("/create-user")
    public List<User> createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
}
