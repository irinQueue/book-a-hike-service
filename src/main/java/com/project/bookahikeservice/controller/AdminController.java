package com.project.bookahikeservice.controller;

import com.project.bookahikeservice.dto.RoleUserCountDto;
import com.project.bookahikeservice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/user-role-stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleUserCountDto>> getUserCountsPerRole() {
        List<RoleUserCountDto> stats = roleService.getUserCountsPerRole();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> helloAdmin() {
        return ResponseEntity.ok("This API is for admin");
    }


}
