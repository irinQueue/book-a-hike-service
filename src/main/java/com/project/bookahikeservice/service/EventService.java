package com.project.bookahikeservice.service;

import com.project.bookahikeservice.dto.request.EventRequestDto;
import com.project.bookahikeservice.dto.response.EventResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventService {
    EventResponseDto createEvent(EventRequestDto dto);
    Page<EventResponseDto> getAllEvents(Pageable pageable);
}
