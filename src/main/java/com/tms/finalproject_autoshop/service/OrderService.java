package com.tms.finalproject_autoshop.service;

import com.tms.finalproject_autoshop.exception.CartException;
import com.tms.finalproject_autoshop.model.*;
import com.tms.finalproject_autoshop.repository.CartRepository;
import com.tms.finalproject_autoshop.repository.OrderRepository;
import com.tms.finalproject_autoshop.repository.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tms.finalproject_autoshop.model.OrderStatus.CREATED;

@Data
@Service
public class OrderService {

    private OrderRepository orderRepository;
    private CartRepository cartRepository;
    private UserRepository userRepository;


    public OrderService(OrderRepository orderRepository, CartRepository cartRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    public void checkout(Long userId) throws CartException {
        Cart cart = cartRepository.findById(userId)
                .orElse(new Cart());
        if(cart.getItems().isEmpty()){
            throw new CartException();
        }
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getProduct().getPrice());
            orderItems.add(orderItem);
        }
        order.setOrder(orderItems);
        order.setOrderStatus(CREATED);
        orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

    }

    public void cancelOrder(Long orderId) throws Exception {
        Optional<Order> order = Optional.ofNullable(orderRepository.findById(orderId));
        if(order.isEmpty()){
            throw new Exception("Order not found");
        }
        orderRepository.delete(order.get());

    }

    public boolean changeStatus(Long orderId, OrderStatus newStatus) throws Exception {
        Order order = orderRepository.findById(orderId);
        if(order.getOrder().isEmpty()){
            throw new Exception("Order not found!");
        }
        order.setOrderStatus(newStatus);
        orderRepository.save(order);
        return true;
    }

}
