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
public class BookingResponseDto {
    private UUID bookingId;
    private UUID eventId;
    private Long joinerId;
    private String bookingType;
    private int pax;
    private String contactPerson;
    private String contactNumber;
    private Boolean isActive;
    private Boolean isCancelled;
    private Boolean isDone;
    private String createdBy;
    private String updatedBy;
    private UUID eventBatchId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
