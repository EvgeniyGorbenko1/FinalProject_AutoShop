package com.tms.finalproject_autoshop.controller;

import com.tms.finalproject_autoshop.model.Order;
import com.tms.finalproject_autoshop.model.OrderStatus;
import com.tms.finalproject_autoshop.model.User;
import com.tms.finalproject_autoshop.security.CustomUserDetailService;
import com.tms.finalproject_autoshop.service.EmailService;
import com.tms.finalproject_autoshop.service.OrderService;
import com.tms.finalproject_autoshop.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/order")
@SecurityRequirement(name = "Bearer Authentication")
public class OrderController {

    private final UserService userService;
    private final OrderService orderService;
    private final EmailService emailService;

    public OrderController(OrderService orderService, EmailService emailService, UserService userService) {
        this.orderService = orderService;
        this.emailService = emailService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Order>> getOrders() {
        List<Order> orderList = orderService.getAllOrders();
        return orderList.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(orderList, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    public ResponseEntity<Order> getOwnOrder() {
        Long userId = CustomUserDetailService.getUserId();
        return orderService.getOrderByUserId(userId)
                .map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.findByOrderId(id)
                .map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/checkout")
    public ResponseEntity<Void> checkout() {
        Long userId = CustomUserDetailService.getUserId();
        Optional<User> user = userService.getUserById(userId);

        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            orderService.checkout(userId);
            emailService.sendEmail(
                    user.get().getEmail(),
                    user.get().getFirstName() + " Your order has been placed",
                    "Thank you for your trust!"
            );
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Checkout error", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<Void> cancelById(@PathVariable Long orderId) {
        return orderService.cancelOrderById(orderId).isPresent()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/cancel")
    public ResponseEntity<Void> cancelOwnOrder() {
        Long userId = CustomUserDetailService.getUserId();
        return orderService.cancelOrderByUserId(userId).isPresent()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        try {
            orderService.changeStatus(orderId, OrderStatus.valueOf(status));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Status update error", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
