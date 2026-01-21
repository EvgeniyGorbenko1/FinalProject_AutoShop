package com.tms.finalproject_autoshop.repository;

import com.tms.finalproject_autoshop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUserId(Long userId);

    Optional<Order> findById(Long orderId);

}
