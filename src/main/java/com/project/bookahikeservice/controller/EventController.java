package com.project.bookahikeservice.controller;

import com.project.bookahikeservice.dto.request.EventRequestDto;
import com.project.bookahikeservice.dto.response.EventResponseDto;
import com.project.bookahikeservice.dto.response.PaginatedResponse;
import com.project.bookahikeservice.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create-event")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<PaginatedResponse<EventResponseDto>> createEvent(@RequestBody @Valid EventRequestDto dto) {
        try {
            EventResponseDto createdEvent = eventService.createEvent(dto);

            PaginatedResponse<EventResponseDto> response = new PaginatedResponse<>(
                    List.of(createdEvent),
                    null,
                    null
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            PaginatedResponse<EventResponseDto> errorResponse = new PaginatedResponse<>(
                    null,
                    null,
                    List.of("Failed to create event: " + e.getMessage())
            );

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }


    // Public access
    @GetMapping("/get-all")
    public ResponseEntity<PaginatedResponse<EventResponseDto>> getAllEvents(@PageableDefault(size = 10) Pageable pageable) {
        try {
            Page<EventResponseDto> page = eventService.getAllEvents(pageable);

            PaginatedResponse.PageableDetails details = new PaginatedResponse.PageableDetails(
                    page.getNumber(),
                    page.getSize(),
                    page.getTotalElements(),
                    page.getTotalPages(),
                    page.isLast()
            );

            PaginatedResponse<EventResponseDto> response = new PaginatedResponse<>(
                    page.getContent(),
                    details,
                    null
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            PaginatedResponse<EventResponseDto> errorResponse = new PaginatedResponse<>(
                    null,
                    null,
                    List.of("Failed to fetch events: " + e.getMessage())
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

}