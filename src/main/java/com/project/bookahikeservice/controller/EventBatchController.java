package com.project.bookahikeservice.controller;

import com.project.bookahikeservice.entity.EventBatch;
import com.project.bookahikeservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/eventBatch")
@RequiredArgsConstructor
public class EventBatchController {

    @Autowired
    private EventService eventService;

    @PostMapping("/{eventId}/batches")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<String> addEventBatch(
            @PathVariable UUID eventId,
            @RequestParam int maxPax
    ) {
        EventBatch newBatch = eventService.addEventBatch(eventId, maxPax);
        return ResponseEntity.ok("New Batch added to the Event Batch List: " + newBatch.getEvent().getTitle());
    }
}
