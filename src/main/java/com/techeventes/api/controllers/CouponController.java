package com.techeventes.api.controllers;

import com.techeventes.api.domain.coupon.Coupon;
import com.techeventes.api.domain.coupon.CouponRequestDTO;

import org.springframework.http.ResponseEntity;
import com.techeventes.api.services.CouponService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/coupon")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/event/{eventId}")
    public ResponseEntity<Coupon> addCouponsToEvent(@PathVariable UUID eventId, @RequestBody CouponRequestDTO body) {
        Coupon coupon = couponService.addCouponsToEvent(eventId, body);
        return ResponseEntity.ok(coupon);
    }
}
