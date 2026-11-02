package com.foodease.service.implementation;

import com.foodease.dto.CouponInfoDTO;
import com.foodease.dto.CouponValidateResponse;
import com.foodease.model.Coupon;
import com.foodease.repository.CouponRepository;
import com.foodease.service.interfaces.CouponService;
import com.foodease.service.strategy.DiscountStrategy;
import com.foodease.service.strategy.NoDiscountStrategy;
import com.foodease.service.strategy.ThresholdDiscountStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    public CouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public List<CouponInfoDTO> getAllCoupons() {
        return couponRepository.findAll().stream()
                .filter(Coupon::isActive)
                .map(CouponInfoDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public CouponValidateResponse validateCoupon(String code, double subtotal) {
        if (code == null || code.trim().isEmpty()) {
            return new CouponValidateResponse(false, "Coupon code is required");
        }
        Optional<Coupon> opt = couponRepository.findByCodeIgnoreCaseAndActiveTrue(code.trim());
        if (opt.isEmpty()) {
            return new CouponValidateResponse(false, "Invalid coupon code");
        }
        Coupon coupon = opt.get();
        if (subtotal < coupon.getMinOrder()) {
            return new CouponValidateResponse(false, "Coupon valid on orders above ₹" + (int) coupon.getMinOrder());
        }

        double discount = coupon.calculateDiscount(subtotal);
        String msg = coupon.getCode() + " applied — " + (int) coupon.getPercentage() + "% off";
        return new CouponValidateResponse(true, msg, coupon.getCode(), coupon.getPercentage(), discount);
    }

    @Override
    public DiscountStrategy resolveDiscountStrategy(String couponCode, double subtotal) {
        if (couponCode == null || couponCode.trim().isEmpty()) {
            return new NoDiscountStrategy();
        }
        return couponRepository.findByCodeIgnoreCaseAndActiveTrue(couponCode.trim())
                .map(c -> (DiscountStrategy) new ThresholdDiscountStrategy(c.getCode(), c.getPercentage(), c.getMinOrder()))
                .orElseGet(NoDiscountStrategy::new);
    }
}
