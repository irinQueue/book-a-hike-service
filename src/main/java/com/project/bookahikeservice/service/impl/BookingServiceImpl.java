package com.project.bookahikeservice.service.impl;

import com.project.bookahikeservice.dto.request.BookingRequestDto;
import com.project.bookahikeservice.dto.response.BookingResponseDto;
import com.project.bookahikeservice.entity.Booking;
import com.project.bookahikeservice.entity.Event;
import com.project.bookahikeservice.entity.User;
import com.project.bookahikeservice.repository.BookingRepository;
import com.project.bookahikeservice.repository.EventRepository;
import com.project.bookahikeservice.repository.UserRepository;
import com.project.bookahikeservice.service.BookingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;


    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }

        User userDetails = (User) authentication.getPrincipal();

        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userDetails.getId()));
    }

    @Override
    public BookingResponseDto createBooking(BookingRequestDto dto) {

        User user = getCurrentUser();
        String currentUser = user != null ? user.getUsername() : "guest";
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new NoSuchElementException("Event not found"));

        User joiner = null;
        if (dto.getJoinerId() != null) {
            joiner = userRepository.findById(dto.getJoinerId())
                    .orElseThrow(() -> new NoSuchElementException("User not found"));
        }

        Booking booking = Booking.builder()
                .event(event)
                .joiner(joiner)
                .bookingType(dto.getBookingType())
                .pax(dto.getPax())
                .contactPerson(dto.getContactPerson())
                .contactNumber(dto.getContactNumber())
                .createdBy(currentUser)
                .build();

        booking = bookingRepository.save(booking);

        return toDto(booking);
    }

    @Override
    public BookingResponseDto updateBooking(UUID bookingId, BookingRequestDto dto) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException("Booking not found"));

        User user = getCurrentUser();
        String currentUser = user != null ? user.getUsername() : "guest";

        booking.setPax(dto.getPax());
        booking.setContactPerson(dto.getContactPerson());
        booking.setContactNumber(dto.getContactNumber());
        booking.setBookingType(dto.getBookingType());
        booking.setUpdatedBy(currentUser);

        if (dto.getJoinerId() != null) {
            User joiner = userRepository.findById(dto.getJoinerId())
                    .orElseThrow(() -> new NoSuchElementException("User not found"));
            booking.setJoiner(joiner);
        }

        return toDto(bookingRepository.save(booking));
    }

    @Override
    public String deleteBooking(UUID bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new NoSuchElementException("Booking not found");
        }
        bookingRepository.deleteById(bookingId);
        return "Booking has been deleted";
    }

    @Override
    public BookingResponseDto getBookingById(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException("Booking not found"));
        return toDto(booking);
    }

    @Override
    public List<BookingResponseDto> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private BookingResponseDto toDto(Booking booking) {
        return BookingResponseDto.builder()
                .bookingId(booking.getBookingId())
                .eventId(booking.getEvent().getId())
                .joinerId(booking.getJoiner() != null ? booking.getJoiner().getId() : null)
                .bookingType(booking.getBookingType())
                .pax(booking.getPax())
                .contactPerson(booking.getContactPerson())
                .contactNumber(booking.getContactNumber())
                .createdBy(booking.getCreatedBy())
                .updatedBy(booking.getUpdatedBy())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }
}
