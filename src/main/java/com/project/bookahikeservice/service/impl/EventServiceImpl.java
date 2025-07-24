package com.project.bookahikeservice.service.impl;

import com.project.bookahikeservice.dto.request.EventRequestDto;
import com.project.bookahikeservice.dto.response.EventResponseDto;
import com.project.bookahikeservice.entity.Event;
import com.project.bookahikeservice.entity.EventBatch;
import com.project.bookahikeservice.entity.User;
import com.project.bookahikeservice.repository.EventBatchRepository;
import com.project.bookahikeservice.repository.EventRepository;
import com.project.bookahikeservice.repository.UserRepository;
import com.project.bookahikeservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    @Autowired
    private  EventRepository eventRepository;

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private EventBatchRepository eventBatchRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }

        User userDetails = (User) authentication.getPrincipal();

        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userDetails.getId()));
    }

    @Override
    public EventResponseDto createEvent(EventRequestDto dto) {
        User coordinator = userRepository.findById(dto.getCoordinatorId())
                .orElseThrow(() -> new RuntimeException("Coordinator not found"));

        User currentUser = getCurrentUser();

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
                .active(true)
                .createdBy(currentUser)
                .updatedBy(currentUser)
                .build();

        Event saved = eventRepository.save(event);

        return mapToResponse(saved);
    }

    @Override
    public EventResponseDto updateEvent(UUID id, EventRequestDto dto) {
        Event event = eventRepository.findByIdOrderByCreatedAt(id)
                .orElseThrow(() -> new NoSuchElementException("Event not found with ID: " + id));

        User coordinator = userRepository.findById(dto.getCoordinatorId())
                .orElseThrow(() -> new RuntimeException("Coordinator not found"));

        User currentUser = getCurrentUser();

        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setStartDate(dto.getStartDate());
        event.setEndDate(dto.getEndDate());
        event.setDifficulty(dto.getDifficulty());
        event.setClassification(dto.getClassification());
        event.setCost(dto.getCost());
        event.setCoordinator(coordinator);
        event.setImages(dto.getImages());
        event.setUpdatedBy(currentUser);
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
    public EventResponseDto getEventById(UUID id) {
        Event event = eventRepository.findByIdOrderByCreatedAt(id)
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

    @Override
    public String disableEvent(UUID id) {
        Event event = eventRepository.findByIdOrderByCreatedAt(id)
                .orElseThrow(() -> new NoSuchElementException("Event not found with ID: " + id));
        event.setActive(false);
        eventRepository.save(event);
        return "Event disabled successfully.";
    }

    @Override
    public String deleteEvent(UUID id) {
        Event event = eventRepository.findByIdOrderByCreatedAt(id)
                .orElseThrow(() -> new NoSuchElementException("Event not found with ID: " + id));
        eventRepository.delete(event);
        return "Event deleted successfully.";
    }

    @Override
    public Page<EventResponseDto> getAllActiveEvents(Pageable pageable) {
        return eventRepository.findAllByActiveTrue(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<EventResponseDto> getAllInactiveEvents(Pageable pageable) {
        return eventRepository.findAllByActiveFalse(pageable)
                .map(this::mapToResponse);
    }

    public EventBatch addEventBatch(UUID eventId, int maxPax) {
        Event event = eventRepository.findByIdOrderByCreatedAt(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        EventBatch newBatch = EventBatch.builder()
                .event(event)
                .maxPax(maxPax)
                .currentPax(0)
                .build();

        return eventBatchRepository.save(newBatch);
    }


}
