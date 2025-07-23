package com.project.bookahikeservice.controller;

import com.project.bookahikeservice.dto.request.BookingRequestDto;
import com.project.bookahikeservice.dto.response.BookingFilter;
import com.project.bookahikeservice.dto.response.BookingResponseDto;
import com.project.bookahikeservice.dto.response.EventResponseDto;
import com.project.bookahikeservice.dto.response.PaginatedResponse;
import com.project.bookahikeservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<PaginatedResponse<BookingResponseDto>> getAllBookings(
            @RequestParam(required = false) UUID eventId,
            @RequestParam(required = false) String bookingType,
            @RequestParam(required = false) Long joinerId,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Boolean isCancelled,
            @RequestParam(required = false) Boolean isDone,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedAfter,
            @PageableDefault Pageable pageable
    ) {
//        BookingFilter filter = new BookingFilter(eventId, bookingType, joinerId, isActive, isCancelled, isDone, createdAfter, updatedAfter);
//        return ResponseEntity.ok(bookingService.getAllBookings(filter));

        try {
            BookingFilter filter = new BookingFilter(eventId, bookingType, joinerId, isActive, isCancelled, isDone, createdAfter, updatedAfter);
            Page<BookingResponseDto> page = bookingService.getAllBookings(pageable,filter);
            String message = "All bookings  successfully retrieved";
            PaginatedResponse<BookingResponseDto> response = getBookingResponseDtoPaginatedResponse(page,message);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new PaginatedResponse<>(null, null, List.of("Failed to fetch all bookings: " + e.getMessage()), null)
            );
        }
    }

    @GetMapping("/get-all-active-booking")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<PaginatedResponse<BookingResponseDto>> getAllActiveBookings(@PageableDefault Pageable pageable) {

        try {

            Page<BookingResponseDto> page = bookingService.getAllActiveBookings(pageable);
            String message = "Active bookings successfully retrieved";
            PaginatedResponse<BookingResponseDto> response = getBookingResponseDtoPaginatedResponse(page,message);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new PaginatedResponse<>(null, null, List.of("Failed to fetch active events: " + e.getMessage()), null)
            );
        }
    }



    @GetMapping("/get-all-past-booking")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<List<BookingResponseDto>> getAllPastBookings() {
        return ResponseEntity.ok(bookingService.getAllPastBookings());
    }

    @GetMapping("/get-all-cancelled-booking")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<List<BookingResponseDto>> getAllCancelledBookings() {
        return ResponseEntity.ok(bookingService.getAllCancelledBookings());
    }

    private static PaginatedResponse<BookingResponseDto> getBookingResponseDtoPaginatedResponse(Page<BookingResponseDto> page,String message) {
        PaginatedResponse.PageableDetails details = new PaginatedResponse.PageableDetails(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );

        return new PaginatedResponse<>(
                page.getContent(),
                details,
                null,
                message
        );
    }

}
