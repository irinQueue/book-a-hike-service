package com.project.bookahikeservice.service;

import com.project.bookahikeservice.dto.request.BookingRequestDto;
import com.project.bookahikeservice.dto.response.BookingFilter;
import com.project.bookahikeservice.dto.response.BookingResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BookingService {
    BookingResponseDto createBooking(BookingRequestDto dto);
    BookingResponseDto updateBooking(UUID bookingId, BookingRequestDto dto);
    String deleteBooking(UUID bookingId);
    BookingResponseDto getBookingById(UUID bookingId);
    List<BookingResponseDto> getAllCancelledBookings();
    Page<BookingResponseDto> getAllActiveBookings(Pageable pageable);
    List<BookingResponseDto> getAllPastBookings();
    Page<BookingResponseDto> getAllBookings(Pageable pageable, BookingFilter bookingFilter);
    List<BookingResponseDto> getAllBookingsByUserId(Long userId);
    List<BookingResponseDto> getAllBookingsByEventId(UUID eventId);


}
