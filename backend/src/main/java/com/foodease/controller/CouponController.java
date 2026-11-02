package com.foodease.controller;

import com.foodease.dto.CouponInfoDTO;
import com.foodease.dto.CouponValidateRequest;
import com.foodease.dto.CouponValidateResponse;
import com.foodease.service.interfaces.CouponService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping
    public List<CouponInfoDTO> getCoupons() {
        return couponService.getAllCoupons();
    }

    @PostMapping("/validate")
    public CouponValidateResponse validateCoupon(@Valid @RequestBody CouponValidateRequest request) {
        return couponService.validateCoupon(request.getCode(), request.getSubtotal());
    }
}
