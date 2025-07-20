package com.project.bookahikeservice.service;

import com.project.bookahikeservice.dto.request.EventRequestDto;
import com.project.bookahikeservice.dto.response.EventResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EventService {
    EventResponseDto createEvent(EventRequestDto dto);
    Page<EventResponseDto> getAllEvents(Pageable pageable);
    EventResponseDto getEventById(UUID id);
    EventResponseDto updateEvent(UUID id, EventRequestDto dto);

}
