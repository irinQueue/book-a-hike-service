package com.project.bookahikeservice.repository;

import com.project.bookahikeservice.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
