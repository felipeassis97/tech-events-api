package com.techeventes.api.services;

import com.techeventes.api.domain.coupon.Coupon;
import com.techeventes.api.domain.coupon.CouponRequestDTO;
import com.techeventes.api.repositories.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class CouponService {
    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public Coupon addCouponsToEvent(UUID eventId, CouponRequestDTO data) {
        Coupon newCoupon = new Coupon();
        newCoupon.setCode(data.code());
        newCoupon.setDiscount(data.discount());
        newCoupon.setValid(new Date(data.valid()));
        return null;
    }
}

