package com.techeventes.api.controllers;

import com.techeventes.api.domain.coupon.Coupon;
import com.techeventes.api.domain.coupon.CouponRequestDTO;

import org.springframework.http.ResponseEntity;
import com.techeventes.api.services.CouponService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/coupon")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/event/{eventId}")
    public ResponseEntity<Coupon> addCouponsToEvent(@PathVariable UUID eventId, CouponRequestDTO body) {
        Coupon coupon = couponService.addCouponsToEvent(eventId, body);
        return ResponseEntity.ok(coupon);
    }
}
