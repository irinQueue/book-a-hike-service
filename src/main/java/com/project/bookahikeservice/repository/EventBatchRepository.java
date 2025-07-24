package com.project.bookahikeservice.repository;

import com.project.bookahikeservice.entity.EventBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventBatchRepository extends JpaRepository<EventBatch, UUID> {

    List<EventBatch> findByEventId(UUID eventId);
}