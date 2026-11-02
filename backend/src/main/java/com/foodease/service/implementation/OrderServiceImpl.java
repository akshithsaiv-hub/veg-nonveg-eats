package com.foodease.service.implementation;

import com.foodease.dto.CustomerInfoDTO;
import com.foodease.dto.OrderCreateRequest;
import com.foodease.dto.OrderDTO;
import com.foodease.enums.PaymentMode;
import com.foodease.exception.BadRequestException;
import com.foodease.exception.ResourceNotFoundException;
import com.foodease.model.*;
import com.foodease.repository.CartRepository;
import com.foodease.repository.CustomerRepository;
import com.foodease.repository.OrderRepository;
import com.foodease.service.interfaces.CouponService;
import com.foodease.service.interfaces.OrderService;
import com.foodease.service.strategy.DiscountStrategy;
import com.foodease.service.strategy.payment.PaymentProcessor;
import com.foodease.service.strategy.payment.PaymentProcessorFactory;
import com.foodease.util.OrderIdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Order processing service demonstrating:
 * - Method Overloading (calculateTotal with 1, 2, and 3 parameters)
 * - Strategy pattern polymorphism for discounts and payments
 * - Composition: Order -> OrderItems
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final double DEFAULT_GST_RATE = 5.0;

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final CouponService couponService;
    private final PaymentProcessorFactory paymentProcessorFactory;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CartRepository cartRepository,
                            CustomerRepository customerRepository,
                            CouponService couponService,
                            PaymentProcessorFactory paymentProcessorFactory) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.couponService = couponService;
        this.paymentProcessorFactory = paymentProcessorFactory;
    }

    // Method Overloading #1: subtotal with default 0 discount and default GST
    @Override
    public double calculateTotal(double subtotal) {
        return calculateTotal(subtotal, 0.0, DEFAULT_GST_RATE);
    }

    // Method Overloading #2: subtotal with discount amount and default GST
    @Override
    public double calculateTotal(double subtotal, double discountAmount) {
        return calculateTotal(subtotal, discountAmount, DEFAULT_GST_RATE);
    }

    // Method Overloading #3: fully parameterized financial calculation
    @Override
    public double calculateTotal(double subtotal, double discountAmount, double gstRate) {
        double taxable = Math.max(0.0, subtotal - discountAmount);
        double gst = Math.round((taxable * (gstRate / 100.0)) * 100.0) / 100.0;
        return Math.round((taxable + gst) * 100.0) / 100.0;
    }

    @Override
    public OrderDTO placeOrder(String cartId, OrderCreateRequest request) {
        if (cartId == null || !cartRepository.existsById(cartId)) {
            throw new BadRequestException("Cart is empty");
        }
        Cart cart = cartRepository.findById(cartId).get();
        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        CustomerInfoDTO cDto = request.getCustomer();
        Customer customer = customerRepository.findByPhone(cDto.getPhone())
                .orElseGet(() -> new Customer(cDto.getName(), cDto.getPhone(), cDto.getAddress()));
        customer.setDeliveryAddress(cDto.getAddress());
        customerRepository.save(customer);

        PaymentMode mode = PaymentMode.fromString(cDto.getPaymentMode());

        String orderId = OrderIdGenerator.generateOrderId();
        Order order = new Order(orderId, customer, mode);

        for (CartItem ci : cart.getItems()) {
            OrderItem oi = new OrderItem(order, ci.getMenuItem(), ci.getQuantity());
            order.addOrderItem(oi);
        }

        // Apply discount strategy via Polymorphism
        double subtotal = cart.getSubtotal();
        DiscountStrategy strategy = couponService.resolveDiscountStrategy(request.getCouponCode(), subtotal);
        double discount = strategy.calculateDiscount(subtotal);
        double discountRate = subtotal > 0 ? (discount / subtotal) * 100.0 : 0.0;

        order.calculateFinancials(discountRate, DEFAULT_GST_RATE);
        if (strategy.isApplicable(subtotal)) {
            order.setCouponCode(request.getCouponCode());
        }

        // Process payment via Polymorphic Strategy Factory
        PaymentProcessor processor = paymentProcessorFactory.getProcessor(mode);
        processor.processPayment(order);

        Order savedOrder = orderRepository.save(order);

        // Clear cart after successful checkout
        cart.clear();
        cartRepository.save(cart);

        return new OrderDTO(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order " + orderId + " not found"));
        return new OrderDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }
}
