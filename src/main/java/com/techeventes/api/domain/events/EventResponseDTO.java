package com.techeventes.api.domain.events;

import com.techeventes.api.domain.coupon.CouponResponseDTO;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record EventResponseDTO(
        UUID id,
        String title,
        String description,
        Date date,
        String city,
        String state,
        Boolean remote,
        String eventUrl,
        String imageUrl
) {
}
