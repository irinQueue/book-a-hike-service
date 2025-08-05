package com.project.bookahikeservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bookahikeservice.dto.request.EventRequestDto;
import com.project.bookahikeservice.dto.response.EventResponseDto;
import com.project.bookahikeservice.dto.response.PaginatedResponse;
import com.project.bookahikeservice.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(value = "/create-event", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<?> createEvent(
            @RequestParam("dto") String dtoJson,
            @RequestPart(value = "images", required = false) MultipartFile[] images
    ) {
        try {
            EventRequestDto dto = objectMapper.readValue(dtoJson, EventRequestDto.class);
            EventResponseDto createdEvent = eventService.createEvent(dto, images);

            PaginatedResponse<EventResponseDto> response = new PaginatedResponse<>(
                    List.of(createdEvent), null, null, null);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new PaginatedResponse<>(null, null,
                            List.of("Failed to create event: " + e.getMessage()), null));
        }
    }


    @PatchMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<?> updateEvent(
            @PathVariable UUID id,
            @RequestPart("dto") @Valid EventRequestDto updatedEventDto,
            @RequestPart(value = "images", required = false) MultipartFile[] images
    ) {
        try {
            EventResponseDto updatedEvent = eventService.updateEvent(id, updatedEventDto, images);
            return ResponseEntity.ok(updatedEvent);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(
                    new PaginatedResponse<>(null, null, List.of("Event not found with ID: " + id), null)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new PaginatedResponse<>(null, null, List.of("Failed to update event: " + e.getMessage()), null)
            );
        }
    }


    @PatchMapping("/disable/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<?> disableEvent(@PathVariable UUID id) {
        try {
            String message = eventService.disableEvent(id);
            return ResponseEntity.ok(new PaginatedResponse<>(null, null, null, message));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(
                    new PaginatedResponse<>(null, null, List.of("Event not found with ID: " + id), null)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new PaginatedResponse<>(null, null, List.of("Failed to disable event: " + e.getMessage()), null)
            );
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<?> deleteEvent(@PathVariable UUID id) {
        try {
            String message = eventService.deleteEvent(id);
            return ResponseEntity.ok(new PaginatedResponse<>(null, null, null, message));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(
                    new PaginatedResponse<>(null, null, List.of("Event not found with ID: " + id), null)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new PaginatedResponse<>(null, null, List.of("Failed to delete event: " + e.getMessage()), null)
            );
        }
    }


    @GetMapping("/get-all") // this is for authorized roles api only
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<PaginatedResponse<EventResponseDto>> getAllEvents(@PageableDefault Pageable pageable) {
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
                    null,
                    null
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            PaginatedResponse<EventResponseDto> errorResponse = new PaginatedResponse<>(
                    null,
                    null,
                    List.of("Failed to fetch events: " + e.getMessage()),
                    null
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<?> getEventById(@RequestParam UUID id) {
        try {
            EventResponseDto event = eventService.getEventById(id);

            return ResponseEntity.ok(event); // Returning a single EventResponseDto

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(
                    new PaginatedResponse<>(null, null, List.of("Event not found with ID: " + id), null)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new PaginatedResponse<>(null, null, List.of("Error fetching event: " + e.getMessage()), null)
            );
        }
    }

    @GetMapping("/get-active")
    // this should be client facing api for homepage
    public ResponseEntity<PaginatedResponse<EventResponseDto>> getAllActiveEvents(@PageableDefault Pageable pageable) {
        try {
            Page<EventResponseDto> page = eventService.getAllActiveEvents(pageable);

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
                    null,
                    "Active events fetched successfully."
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new PaginatedResponse<>(null, null, List.of("Failed to fetch active events: " + e.getMessage()), null)
            );
        }
    }

    @GetMapping("/get-inactive")
    // this should be client facing api for homepage
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<PaginatedResponse<EventResponseDto>> getAllInactiveEvents(@PageableDefault Pageable pageable) {
        try {
            Page<EventResponseDto> page = eventService.getAllInactiveEvents(pageable);

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
                    null,
                    "Inactive events fetched successfully."
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new PaginatedResponse<>(null, null, List.of("Failed to fetch active events: " + e.getMessage()), null)
            );
        }
    }

}