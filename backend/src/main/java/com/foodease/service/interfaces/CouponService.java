package com.foodease.service.interfaces;

import com.foodease.dto.CouponInfoDTO;
import com.foodease.dto.CouponValidateResponse;
import com.foodease.service.strategy.DiscountStrategy;
import java.util.List;

public interface CouponService {
    List<CouponInfoDTO> getAllCoupons();
    CouponValidateResponse validateCoupon(String code, double subtotal);
    DiscountStrategy resolveDiscountStrategy(String couponCode, double subtotal);
}
