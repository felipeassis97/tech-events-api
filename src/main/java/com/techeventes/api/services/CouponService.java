package com.techeventes.api.services;

import com.techeventes.api.domain.address.Address;
import com.techeventes.api.domain.coupon.Coupon;
import com.techeventes.api.domain.coupon.CouponRequestDTO;
import com.techeventes.api.domain.coupon.CouponResponseDTO;
import com.techeventes.api.domain.events.Event;
import com.techeventes.api.domain.events.EventDetailsResponseDTO;
import com.techeventes.api.repositories.CouponRepository;
import com.techeventes.api.repositories.EventRepository;
import jakarta.persistence.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CouponService {
    private final CouponRepository couponRepository;
    private final EventRepository eventService;

    public CouponService(CouponRepository couponRepository, EventRepository eventService) {
        this.couponRepository = couponRepository;
        this.eventService = eventService;
    }

    public Coupon addCouponsToEvent(UUID eventId, CouponRequestDTO data) {
        Event currentEvent = eventService.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event with id " + eventId + " not found"));

        Coupon newCoupon = new Coupon();
        newCoupon.setCode(data.code());
        newCoupon.setDiscount(data.discount());
        newCoupon.setValid(new Date(data.valid()));
        newCoupon.setEvent(currentEvent);

        return couponRepository.save(newCoupon);
    }

    public List<CouponResponseDTO> getCouponsByEventId(UUID eventId, Date currentDate) {
        List<Coupon> coupons = couponRepository.findByEventIdAndValidAfter(eventId, currentDate);
        return coupons.stream().map(item -> new CouponResponseDTO(
                item.getId(),
                item.getCode(),
                item.getDiscount(),
                item.getValid()
        )).toList();
    }
}

