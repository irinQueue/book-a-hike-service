package com.project.bookahikeservice.controller;


import com.project.bookahikeservice.dto.ApiResponse;
import com.project.bookahikeservice.entity.User;
import com.project.bookahikeservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping
    public ApiResponse<User> getAllUsers() {
        List<User> users = userService.getUser(); // assuming this returns all users
        return new ApiResponse<>(users, Collections.emptyList());
    }

    @PostMapping("/create-user")
    public ApiResponse<User> createUser(@RequestBody User user) {
        try {
            User savedUser = userService.saveUser(user);
            return new ApiResponse<>(
                    Collections.singletonList(savedUser),
                    Collections.emptyList()
            );
        } catch (Exception e) {
            return new ApiResponse<>(
                    Collections.emptyList(),
                    Collections.singletonList("Failed to save user: " + e.getMessage())
            );
        }
    }

}
