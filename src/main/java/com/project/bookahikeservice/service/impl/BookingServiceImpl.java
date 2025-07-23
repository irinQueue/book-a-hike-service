package com.project.bookahikeservice.service.impl;

import com.project.bookahikeservice.dto.request.BookingRequestDto;
import com.project.bookahikeservice.dto.response.BookingFilter;
import com.project.bookahikeservice.dto.response.BookingResponseDto;
import com.project.bookahikeservice.entity.Booking;
import com.project.bookahikeservice.entity.Event;
import com.project.bookahikeservice.entity.User;
import com.project.bookahikeservice.repository.BookingRepository;
import com.project.bookahikeservice.repository.EventRepository;
import com.project.bookahikeservice.repository.UserRepository;
import com.project.bookahikeservice.service.BookingService;
import com.project.bookahikeservice.specification.BookingSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return null; // Guest booking
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserDetails)) {
            throw new RuntimeException("Invalid authenticated principal");
        }

        UserDetails userDetails = (User) principal;

        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userDetails.getUsername()));
    }


    @Override
    public BookingResponseDto createBooking(BookingRequestDto dto) {
        User user = getCurrentUser();
        boolean isLogin = user != null;
        String currentUser = isLogin ? user.getEmail() : null;
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new NoSuchElementException("Event not found"));

        User joiner = null;
        if (currentUser != null) {
            joiner = userRepository.findByEmail(currentUser)
                    .orElseThrow(() -> new NoSuchElementException("User not found"));

        }

        Booking booking = Booking.builder()
                .event(event)
                .joiner(joiner)
                .bookingType(currentUser == null ? "GUEST" : "ACCOUNT")
                .pax(dto.getPax())
                .contactPerson(dto.getContactPerson())
                .contactNumber(dto.getContactNumber())
                .isActive(true)
                .isCancelled(false)
                .isDone(false)
                .createdBy(currentUser == null ? dto.getContactPerson() : joiner.getFirstName() + " " + joiner.getLastName())
                .build();

        booking = bookingRepository.save(booking);

        return toDto(booking);
    }

    @Override
    public BookingResponseDto updateBooking(UUID bookingId, BookingRequestDto dto) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException("Booking not found"));

        User user = getCurrentUser();
        String currentUser = user != null ? user.getEmail() : null;

        booking.setPax(dto.getPax());
        booking.setContactPerson(dto.getContactPerson());
        booking.setContactNumber(dto.getContactNumber());

        if (currentUser != null) {
            User joiner = userRepository.findByEmail(currentUser)
                    .orElseThrow(() -> new NoSuchElementException("User not found"));
            booking.setUpdatedBy((joiner.getFirstName() + " " + joiner.getLastName()));
            booking.setBookingType("ACCOUNT");
            booking.setJoiner(joiner);
        } else {
            booking.setBookingType("GUEST");
            booking.setJoiner(null);
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
    public Page<BookingResponseDto> getAllBookings(Pageable pageable, BookingFilter bookingFilter) {
        Page<Booking> bookings = bookingRepository.findAll(BookingSpecifications.withFilters(bookingFilter), pageable);
        return bookings.map(this::toDto);


    }


    @Override
    public Page<BookingResponseDto> getAllBookingsByUserId(Pageable pageable, Long joinerId) {
        return bookingRepository.findBookingByJoinerId(pageable,joinerId)
                .map(this::toDto);
    }

    @Override
    public Page<BookingResponseDto> getAllBookingsByEventId(Pageable pageable, UUID eventId) {
        return bookingRepository.findBookingByEventId(pageable,eventId)
                .map(this::toDto);
    }

    @Override
    public Page<BookingResponseDto> getAllCancelledBookings(Pageable pageable) {
        return bookingRepository.findBookingByIsCancelledTrue(pageable)
                .map(this::toDto);

    }

    @Override
    public Page<BookingResponseDto> getAllActiveBookings(Pageable pageable) {
        return bookingRepository.findBookingByIsActiveTrueAndIsCancelledFalseAndIsDoneFalse(pageable)
                .map(this::toDto);
    }

    @Override
    public Page<BookingResponseDto> getAllPastBookings(Pageable pageable) {
        return bookingRepository.findBookingByIsDoneTrue(pageable)
                .map(this::toDto);
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
                .isActive(booking.getIsActive())
                .isCancelled(booking.getIsCancelled())
                .isDone(booking.getIsDone())
                .updatedBy(booking.getUpdatedBy())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }
}
