package com.tms.finalproject_autoshop.controller;

import com.tms.finalproject_autoshop.model.OrderStatus;
import com.tms.finalproject_autoshop.model.User;
import com.tms.finalproject_autoshop.service.EmailService;
import com.tms.finalproject_autoshop.service.OrderService;
import com.tms.finalproject_autoshop.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@Data
@RestController
@RequestMapping("/order")
public class OrderController {

    private final UserService userService;
    private OrderService orderService;
    private EmailService emailService;

    public OrderController(OrderService orderService, EmailService emailService, UserService userService) {
        this.orderService = orderService;
        this.emailService = emailService;
        this.userService = userService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<HttpStatus> checkout(@RequestParam Long userId){
        try{
            Optional<User> user = userService.getUserById(userId);
            orderService.checkout(userId);
            emailService.sendEmail(user.get().getEmail(), user.get().getFirstName() + "Your order has been placed", "Thank you for your trust!");
            return new ResponseEntity<>(HttpStatus.OK);

        }catch(Exception e){
            return new  ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
    }

    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<HttpStatus> cancel(@PathVariable Long orderId){
        try{
            orderService.cancelOrder(orderId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return new  ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<HttpStatus> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status){
        try{
            orderService.changeStatus(orderId, OrderStatus.valueOf(status));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e){
            return new  ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
