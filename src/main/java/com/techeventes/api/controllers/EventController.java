package com.techeventes.api.controllers;

import com.techeventes.api.domain.events.Event;
import com.techeventes.api.domain.events.EventDetailsResponseDTO;
import com.techeventes.api.domain.events.EventRequestDTO;
import com.techeventes.api.domain.events.EventResponseDTO;
import com.techeventes.api.services.EventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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
            @RequestParam("uf") String uf,
            @RequestParam("eventUrl") String eventUrl,
            @RequestParam(value = "remote", required = false) Boolean remote,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "description", required = false) String description
    ) {
        EventRequestDTO eventRequestDTO = new EventRequestDTO(title, description, date, city, uf, remote, eventUrl, image);
        Event newEvent = eventService.createEvent(eventRequestDTO);
        return ResponseEntity.ok(newEvent);
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<EventResponseDTO> allEvents = eventService.getUpcomingEvents(page, size);
        return ResponseEntity.ok(allEvents);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<EventResponseDTO>> filterEvents(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size,
                                                               @RequestParam(required = false) String city,
                                                               @RequestParam(required = false) String uf,
                                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        List<EventResponseDTO> allEvents = eventService.getFilteredEvents(page, size, city, uf, startDate, endDate);
        return ResponseEntity.ok(allEvents);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDetailsResponseDTO> getEventById(@PathVariable UUID eventId) {
        EventDetailsResponseDTO event = eventService.getEventById(eventId);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(event);
    }
}

