package com.techeventes.api.repositories;

import com.techeventes.api.domain.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {
    public List<Coupon> findByEventIdAndValidAfter(UUID eventId, Date currentDate);
}