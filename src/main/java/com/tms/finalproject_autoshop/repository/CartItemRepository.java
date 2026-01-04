package com.tms.finalproject_autoshop.repository;

import com.tms.finalproject_autoshop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    CartItem findByCartIdAndProductId(Long id,  Long productId);
}
