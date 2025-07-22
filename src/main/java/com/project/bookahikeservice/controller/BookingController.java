package com.project.bookahikeservice.controller;

import com.project.bookahikeservice.dto.request.BookingRequestDto;
import com.project.bookahikeservice.dto.response.BookingResponseDto;
import com.project.bookahikeservice.dto.response.PaginatedResponse;
import com.project.bookahikeservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/create-booking")
    public ResponseEntity<BookingResponseDto> createBooking(
            @RequestBody BookingRequestDto dto
    ) {
        return ResponseEntity.ok(bookingService.createBooking(dto));
    }

    @PatchMapping("update-booking/{id}")
    public ResponseEntity<BookingResponseDto> updateBooking(
            @PathVariable UUID id,
            @RequestBody BookingRequestDto dto
    ) {
        return ResponseEntity.ok(bookingService.updateBooking(id, dto));
    }

    @DeleteMapping("/delete-booking/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<?> deleteBooking(@PathVariable UUID id) {
        try {
            String message = bookingService.deleteBooking(id);
            return ResponseEntity.ok(new PaginatedResponse<>(null, null, null, message));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(
                    new PaginatedResponse<>(null, null, List.of("Booking not found with ID: " + id), null)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new PaginatedResponse<>(null, null, List.of("Failed to delete Booking: " + e.getMessage()), null)
            );
        }
    }

    @GetMapping("/get-booking/{id}")
    public ResponseEntity<BookingResponseDto> getBookingById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/get-user-booking/{id}")
    public ResponseEntity<List<BookingResponseDto>> getBookingByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getAllBookingsByUserId(id));
    }

    @GetMapping("/get-booking-event/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<List<BookingResponseDto>> getBookingByEventId(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingService.getAllBookingsByEventId(id));
    }

    @GetMapping("/get-all-booking")
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }


}
