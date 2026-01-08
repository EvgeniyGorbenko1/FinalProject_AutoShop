package com.tms.finalproject_autoshop.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "cart")
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<CartItem> items = new ArrayList<>();

    public Double getTotalAmount() {
        double totalAmount = 0.0;
        for (CartItem item : items) {
            totalAmount += item.getTotalPrice();
        }
        return totalAmount;
    }
}
