package com.project.bookahikeservice.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class BookingRequestDto {
    private UUID eventId;
    private int pax;
    private String contactPerson;
    private String contactNumber;
}
