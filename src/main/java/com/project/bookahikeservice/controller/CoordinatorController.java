package com.project.bookahikeservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coordinator")
public class CoordinatorController {
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<String> helloCoordinator() {
        return ResponseEntity.ok("This API is for coordinator");
    }
}
