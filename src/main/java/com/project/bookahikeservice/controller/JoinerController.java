package com.project.bookahikeservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/joiner")
public class JoinerController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('JOINER')")
    public ResponseEntity<String> helloJoiner() {
        return ResponseEntity.ok("This API is for joiner");
    }
}
