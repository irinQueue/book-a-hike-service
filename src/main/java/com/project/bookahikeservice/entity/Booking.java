package com.project.bookahikeservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "booking_id", updatable = false, nullable = false)
    private UUID bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "joiner_id")
    private User joiner; // nullable if booked as guest

    @Column(name = "booking_type", nullable = false)
    private String bookingType; // "GUEST" or "USER"

    private int pax;

    private String contactPerson;
    private String contactNumber;

    // Logging
    private String createdBy;
    private String updatedBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
