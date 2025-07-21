package com.techeventes.api.controllers;

import com.techeventes.api.domain.events.Event;
import com.techeventes.api.domain.events.EventRequestDTO;
import com.techeventes.api.domain.events.EventResponseDTO;
import com.techeventes.api.services.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Event> createEvent(
            @RequestParam("title") String title,
            @RequestParam("date") Long date,
            @RequestParam("city") String city,
            @RequestParam("state") String state,
            @RequestParam("eventUrl") String eventUrl,
            @RequestParam(value = "remote", required = false) Boolean remote,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "description", required = false) String description
    ) {
        EventRequestDTO eventRequestDTO = new EventRequestDTO(title, description, date, city, state, remote, eventUrl, image);
        Event newEvent = eventService.createEvent(eventRequestDTO);
        return ResponseEntity.ok(newEvent);
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<EventResponseDTO> allEvents = eventService.getUpcomingEvents(page, size);
        return ResponseEntity.ok(allEvents);
    }
}

