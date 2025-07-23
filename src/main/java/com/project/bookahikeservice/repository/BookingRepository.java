package com.project.bookahikeservice.repository;

import com.project.bookahikeservice.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID>, JpaSpecificationExecutor<Booking> {
    List<Booking> findBookingByEventId(UUID eventId);
    List<Booking> findBookingByJoinerId(Long joinerId);
    List<Booking> findBookingByIsCancelledTrue(); //cancelled bookings
    List<Booking> findBookingByIsDoneTrue(); // past booking
    List<Booking> findBookingByIsActiveTrueAndIsCancelledFalseAndIsDoneFalse(); // Active booking


}