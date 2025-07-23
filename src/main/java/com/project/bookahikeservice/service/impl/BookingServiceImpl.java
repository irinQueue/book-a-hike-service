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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
                .createdBy(currentUser == null ? dto.getContactPerson() : joiner.getFirstName() +  " " + joiner.getLastName())
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
        }else{
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
    public List<BookingResponseDto> getAllBookings(BookingFilter bookingFilter) {
        List<Booking> bookings = bookingRepository.findAll(BookingSpecifications.withFilters(bookingFilter));
        return bookings.stream()
                .map(this::toDto) // your existing mapper
                .collect(Collectors.toList());
    }


    @Override
    public List<BookingResponseDto> getAllBookingsByUserId(Long joinerId) {
        return bookingRepository.findBookingByJoinerId(joinerId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getAllBookingsByEventId(UUID eventId) {
        return bookingRepository.findBookingByEventId(eventId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getAllCancelledBookings() {
        return bookingRepository.findBookingByIsCancelledTrue()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getAllActiveBookings() {
        return bookingRepository.findBookingByIsActiveTrueAndIsCancelledFalseAndIsDoneFalse()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getAllPastBookings() {
        return bookingRepository.findBookingByIsDoneTrue()
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
                .isActive(booking.getIsActive())
                .isCancelled(booking.getIsCancelled())
                .isDone(booking.getIsDone())
                .updatedBy(booking.getUpdatedBy())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }
}
