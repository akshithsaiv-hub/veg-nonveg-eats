package com.foodease.controller;

import com.foodease.enums.UserRole;
import com.foodease.model.Admin;
import com.foodease.model.Customer;
import com.foodease.model.User;
import com.foodease.repository.CustomerRepository;
import com.foodease.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller demonstrating OOP User hierarchy (User -> Customer, Admin)
 * and Polymorphism via getDashboardAccess().
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public UserController(UserRepository userRepository, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public List<Map<String, Object>> getAllUsers() {
        return userRepository.findAll().stream().map(u -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", u.getId());
            map.put("name", u.getName());
            map.put("role", u.getRole().name());
            // Polymorphic method call
            map.put("dashboard_access", u.getDashboardAccess());
            return map;
        }).collect(Collectors.toList());
    }

    @PostMapping("/customer")
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }
}
