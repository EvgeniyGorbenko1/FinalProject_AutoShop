package com.tms.finalproject_autoshop.repository;

import com.tms.finalproject_autoshop.model.Order;
import com.tms.finalproject_autoshop.model.User;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByUserId(Long userId);

    Order findById(Long orderId);

}
