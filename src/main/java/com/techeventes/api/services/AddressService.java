package com.techeventes.api.services;

import com.techeventes.api.domain.address.Address;
import com.techeventes.api.domain.events.Event;
import com.techeventes.api.domain.events.EventRequestDTO;
import com.techeventes.api.repositories.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address createAddress(EventRequestDTO data, Event event) {
        Address newAddress = new Address();
        newAddress.setCity(data.city());
        newAddress.setUf(data.state());
        newAddress.setEvent(event);
        return addressRepository.save(newAddress);
    }
}
