package com.project.bookahikeservice.service;

import com.project.bookahikeservice.dto.request.EventRequestDto;
import com.project.bookahikeservice.dto.response.EventResponseDto;
import com.project.bookahikeservice.entity.EventBatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface EventService {
    EventResponseDto createEvent(EventRequestDto dto, MultipartFile[] images);
    Page<EventResponseDto> getAllEvents(Pageable pageable);
    EventResponseDto getEventById(UUID id);
    EventResponseDto updateEvent(UUID id, EventRequestDto dto, MultipartFile[] images);
    String disableEvent(UUID id);
    String deleteEvent(UUID id);
    Page<EventResponseDto> getAllActiveEvents(Pageable pageable);
    Page<EventResponseDto> getAllInactiveEvents(Pageable pageable);
    EventBatch addEventBatch (UUID eventId, int maxPax);
}
