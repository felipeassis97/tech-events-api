package com.techeventes.api.services;

import com.amazonaws.services.s3.AmazonS3;
import com.techeventes.api.domain.events.Event;
import com.techeventes.api.domain.events.EventRequestDTO;
import com.techeventes.api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class EventService {

    private final AmazonS3 s3Client;
    private final EventRepository repository;
    Logger logger = Logger.getLogger(getClass().getName());
    @Value("${aws.bucket.name}")
    private String bucketName;

    public EventService(AmazonS3 s3Client, EventRepository repository) {
        this.s3Client = s3Client;
        this.repository = repository;
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

        return repository.save(newEvent);
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
