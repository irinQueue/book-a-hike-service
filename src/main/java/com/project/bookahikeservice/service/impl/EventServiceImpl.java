package com.project.bookahikeservice.service.impl;

import com.project.bookahikeservice.dto.request.EventRequestDto;
import com.project.bookahikeservice.dto.response.EventResponseDto;
import com.project.bookahikeservice.entity.Event;
import com.project.bookahikeservice.entity.User;
import com.project.bookahikeservice.repository.EventRepository;
import com.project.bookahikeservice.repository.UserRepository;
import com.project.bookahikeservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public EventResponseDto createEvent(EventRequestDto dto) {
        User coordinator = userRepository.findById(dto.getCoordinatorId())
                .orElseThrow(() -> new RuntimeException("Coordinator not found"));

        Event event = Event.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .difficulty(dto.getDifficulty())
                .classification(dto.getClassification())
                .cost(dto.getCost())
                .coordinator(coordinator)
                .images(dto.getImages())
                .build();

        Event saved = eventRepository.save(event);

        return mapToResponse(saved);
    }

    @Override
    public EventResponseDto updateEvent(Long id, EventRequestDto dto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Event not found with ID: " + id));

        User coordinator = userRepository.findById(dto.getCoordinatorId())
                .orElseThrow(() -> new RuntimeException("Coordinator not found"));

        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setStartDate(dto.getStartDate());
        event.setEndDate(dto.getEndDate());
        event.setDifficulty(dto.getDifficulty());
        event.setClassification(dto.getClassification());
        event.setCost(dto.getCost());
        event.setCoordinator(coordinator);
        event.setImages(dto.getImages());

        Event saved = eventRepository.save(event);

        return mapToResponse(saved);
    }



    @Override
    public Page<EventResponseDto> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    private EventResponseDto mapToResponse(Event event) {
        EventResponseDto response = new EventResponseDto();
        response.setId(event.getId());
        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setStartDate(event.getStartDate());
        response.setEndDate(event.getEndDate());
        response.setDifficulty(event.getDifficulty());
        response.setClassification(event.getClassification());
        response.setCost(event.getCost());
        response.setCoordinatorName(event.getCoordinator().getFirstName() + " " + event.getCoordinator().getLastName());
        response.setImages(event.getImages());
        return response;
    }

    @Override
    public EventResponseDto getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Event not found with id: " + id));

        return new EventResponseDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getStartDate(),
                event.getEndDate(),
                event.getDifficulty(),
                event.getClassification(),
                event.getCost(),
                event.getCoordinator().getFirstName() + " " + event.getCoordinator().getLastName(),
                event.getImages()
        );
    }

}
