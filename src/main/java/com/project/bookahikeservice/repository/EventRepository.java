package com.project.bookahikeservice.repository;

import com.project.bookahikeservice.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findById(UUID id);
}
