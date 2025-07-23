package com.project.bookahikeservice.repository;

import com.project.bookahikeservice.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID>, JpaSpecificationExecutor<Booking> {
    Page<Booking> findBookingByEventId(Pageable pageable,UUID eventId);
    Page<Booking> findBookingByJoinerId(Pageable pageable, Long joinerId);
    Page<Booking> findBookingByIsCancelledTrue(Pageable pageable); //cancelled bookings
    Page<Booking> findBookingByIsDoneTrue(Pageable pageable); // past booking
    Page<Booking> findBookingByIsActiveTrueAndIsCancelledFalseAndIsDoneFalse(Pageable pageable); // Active booking


}