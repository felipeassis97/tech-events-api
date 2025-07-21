package com.techeventes.api.domain.events;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue
    private UUID id;

    private String title;
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "event_url")
    private String eventUrl;
    private Boolean remote;
    private Date date;
}
