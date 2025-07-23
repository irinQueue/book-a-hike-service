package com.project.bookahikeservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingFilter {
    private UUID eventId;
    private String bookingType;
    private Long joinerId;
    private Boolean isActive;
    private Boolean isCancelled;
    private Boolean isDone;
    private LocalDateTime createdAfter;
    private LocalDateTime updatedAfter;

}
