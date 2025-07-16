package com.project.bookahikeservice.controller;


import com.project.bookahikeservice.dto.ApiResponse;
import com.project.bookahikeservice.dto.UserRegistrationDto;
import com.project.bookahikeservice.dto.UserResponseDto;
import com.project.bookahikeservice.entity.User;
import com.project.bookahikeservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping("/get-all")
    public ApiResponse<UserResponseDto> getAllUsers() {
        List<User> users = userService.getAllUsers();

        List<UserResponseDto> dtoList = users.stream()
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getNumber()
                ))
                .collect(Collectors.toList());

        return new ApiResponse<>(dtoList, Collections.emptyList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponseDto dto = new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getNumber()
        );

        ApiResponse<UserResponseDto> response = new ApiResponse<>(
                Collections.singletonList(dto),
                Collections.emptyList()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-user")
    public ApiResponse<User> createUser(@RequestBody UserRegistrationDto userDto) {
        try {
            User savedUser = userService.saveUser(userDto); // this assigns ROLE_JOINER
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
