package com.project.bookahikeservice.service;

import com.project.bookahikeservice.dto.request.BookingRequestDto;
import com.project.bookahikeservice.dto.response.BookingResponseDto;

import java.util.List;
import java.util.UUID;

public interface BookingService {
    BookingResponseDto createBooking(BookingRequestDto dto);
    BookingResponseDto updateBooking(UUID bookingId, BookingRequestDto dto);
    String deleteBooking(UUID bookingId);
    BookingResponseDto getBookingById(UUID bookingId);
    List<BookingResponseDto> getAllBookings();
}
