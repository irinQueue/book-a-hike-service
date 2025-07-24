package com.project.bookahikeservice.service;

import com.project.bookahikeservice.dto.request.BookingRequestDto;
import com.project.bookahikeservice.dto.response.BookingFilter;
import com.project.bookahikeservice.dto.response.BookingResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BookingService {
    BookingResponseDto createBooking(BookingRequestDto dto);
    BookingResponseDto updateBooking(UUID bookingId, BookingRequestDto dto);
    String deleteBooking(UUID bookingId);
    BookingResponseDto getBookingById(UUID bookingId);
    Page<BookingResponseDto> getAllCancelledBookings(Pageable pageable);
    Page<BookingResponseDto> getAllActiveBookings(Pageable pageable);
    Page<BookingResponseDto> getAllPastBookings(Pageable pageable);
    Page<BookingResponseDto> getAllBookings(Pageable pageable, BookingFilter bookingFilter);
    Page<BookingResponseDto> getAllBookingsByUserId(Pageable pageable, Long userId);
    Page<BookingResponseDto> getAllBookingsByEventId(Pageable pageable, UUID eventId);


}
