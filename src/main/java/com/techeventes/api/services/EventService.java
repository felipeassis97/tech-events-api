package com.techeventes.api.services;

import com.amazonaws.services.s3.AmazonS3;
import com.techeventes.api.domain.coupon.CouponResponseDTO;
import com.techeventes.api.domain.events.Event;
import com.techeventes.api.domain.events.EventDetailsResponseDTO;
import com.techeventes.api.domain.events.EventRequestDTO;
import com.techeventes.api.domain.events.EventResponseDTO;
import com.techeventes.api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class EventService {

    private final AmazonS3 s3Client;
    private final EventRepository repository;
    private final AddressService addressService;
    private final CouponService couponService;

    Logger logger = Logger.getLogger(getClass().getName());
    @Value("${aws.bucket.name}")
    private String bucketName;

    public EventService(AmazonS3 s3Client, EventRepository repository, AddressService addressService, CouponService couponService) {
        this.s3Client = s3Client;
        this.repository = repository;
        this.addressService = addressService;
        this.couponService = couponService;
    }

    public Event createEvent(EventRequestDTO data) {
        String imageUrl = null;
        if (data.image() != null) {
            imageUrl = this.uploadImage(data.image());
        }

        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setEventUrl(data.eventUrl());
        newEvent.setDate(new Date(data.date()));
        newEvent.setImageUrl(imageUrl);
        newEvent.setRemote(data.remote());

        repository.save(newEvent);

        if (!data.remote()) {
            addressService.createAddress(data, newEvent);
        }

        return newEvent;
    }

    public List<EventResponseDTO> getEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> events = repository.findAll(pageable);
        return events.map(item -> new EventResponseDTO(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getDate(),
                item.getAddress() != null ? item.getAddress().getCity() : "",
                item.getAddress() != null ? item.getAddress().getUf() : "",
                item.getRemote(),
                item.getEventUrl(),
                item.getImageUrl()
        )).toList();
    }

    public EventDetailsResponseDTO getEventById(UUID eventId) {
        Event event = repository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event with id " + eventId + " not found"));
        List<CouponResponseDTO> coupons = couponService.getCouponsByEventId(eventId, new Date());

        return new EventDetailsResponseDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getAddress() != null ? event.getAddress().getCity() : "",
                event.getAddress() != null ? event.getAddress().getUf() : "",
                event.getRemote(),
                event.getEventUrl(),
                event.getImageUrl(),
                coupons
        );
    }

    public List<EventResponseDTO> getUpcomingEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> events = repository.findUpcomingEvents(new Date(), pageable);

        return events.map(item -> new EventResponseDTO(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getDate(),
                item.getAddress() != null ? item.getAddress().getCity() : "",
                item.getAddress() != null ? item.getAddress().getUf() : "",
                item.getRemote(),
                item.getEventUrl(),
                item.getImageUrl()
        )).toList();
    }

    public List<EventResponseDTO> getFilteredEvents(
            int page,
            int size,
            String city,
            String uf,
            Date startDate,
            Date endDate) {

        city = city == null ? "" : city;
        uf = uf == null ? "" : uf;
        startDate = startDate == null ? new Date(0) : startDate;
        endDate = endDate == null ? new Date(0) : endDate;

        Pageable pageable = PageRequest.of(page, size);
        Page<Event> events = repository.findFilteredEvents(city, uf, startDate, endDate, pageable);

        return events.map(item -> new EventResponseDTO(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getDate(),
                item.getAddress() != null ? item.getAddress().getCity() : "",
                item.getAddress() != null ? item.getAddress().getUf() : "",
                item.getRemote(),
                item.getEventUrl(),
                item.getImageUrl()
        )).toList();
    }

    private String uploadImage(MultipartFile image) {
        String fileName = UUID.randomUUID() + "-" + image.getOriginalFilename();
        try {
            File file = this.convertMultipartToFile(image);
            s3Client.putObject(bucketName, fileName, file);
            file.delete();
            return s3Client.getUrl(bucketName, fileName).toString();
        } catch (Exception e) {
            logger.warning("Error uploading image" + e.getMessage());
            return null;
        }
    }

    private File convertMultipartToFile(MultipartFile image) throws IOException {
        File convFile = new File(Objects.requireNonNull(image.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(image.getBytes());
        fos.close();
        return convFile;
    }
}
