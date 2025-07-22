package com.techeventes.api.domain.address;

import com.techeventes.api.domain.events.Event;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue
    private UUID id;

    private String city;
    private String uf;

    @OneToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
