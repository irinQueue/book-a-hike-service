package com.project.bookahikeservice.controller;


import com.project.bookahikeservice.dto.request.UserUpdateDto;
import com.project.bookahikeservice.dto.response.ApiResponse;
import com.project.bookahikeservice.dto.request.UserRegistrationDto;
import com.project.bookahikeservice.dto.response.UserResponseDto;
import com.project.bookahikeservice.entity.User;
import com.project.bookahikeservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).build();
        }

        String email = authentication.getName();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        UserResponseDto dto = new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getNumber()
        );

        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/me/edit")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateCurrentUser(@RequestBody UserUpdateDto updateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body(
                    new ApiResponse<>(Collections.emptyList(), Collections.singletonList("Unauthorized"))
            );
        }

        String email = authentication.getName();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(updateDto.getFirstName());
        user.setLastName(updateDto.getLastName());
        user.setNumber(updateDto.getNumber());

        User updatedUser = userService.updateUser(user);

        UserResponseDto dto = new UserResponseDto(
                updatedUser.getId(),
                updatedUser.getFirstName(),
                updatedUser.getLastName(),
                updatedUser.getEmail(),
                updatedUser.getNumber()
        );

        return ResponseEntity.ok(new ApiResponse<>(Collections.singletonList(dto), Collections.emptyList()));
    }


}
