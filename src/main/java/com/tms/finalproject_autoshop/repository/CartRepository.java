package com.tms.finalproject_autoshop.repository;

import com.tms.finalproject_autoshop.model.Cart;
import com.tms.finalproject_autoshop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long>{

    Optional<Cart> findByUserId(Long userId);
}
