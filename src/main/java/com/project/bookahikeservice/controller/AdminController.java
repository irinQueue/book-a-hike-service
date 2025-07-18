package com.project.bookahikeservice.controller;

import com.project.bookahikeservice.dto.projection.RoleUserCountProjection;
import com.project.bookahikeservice.dto.request.UserRegistrationDto;
import com.project.bookahikeservice.dto.response.ApiResponse;
import com.project.bookahikeservice.entity.User;
import com.project.bookahikeservice.service.RoleService;
import com.project.bookahikeservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/user-role-stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleUserCountProjection>> getUserCountsPerRole() {
        List<RoleUserCountProjection> stats = roleService.getUserCountsPerRole();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> helloAdmin() {
        return ResponseEntity.ok("This API is for admin");
    }

    @PostMapping("/create-organizer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<User>> createOrganizer(@RequestBody @Valid UserRegistrationDto dto) {
        User created = userService.createOrganizer(dto);
        return ResponseEntity.ok(new ApiResponse<>(List.of(created), null));
    }



}
