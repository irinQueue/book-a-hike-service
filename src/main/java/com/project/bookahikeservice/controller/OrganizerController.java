package com.project.bookahikeservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/organizer")
public class OrganizerController {


    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<String> helloOrganizer() {
        return ResponseEntity.ok("This API is for Organizer");
    }
}
