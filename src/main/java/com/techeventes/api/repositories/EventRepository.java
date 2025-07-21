package com.techeventes.api.repositories;

import com.techeventes.api.domain.events.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
}