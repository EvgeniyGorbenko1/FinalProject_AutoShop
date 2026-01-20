package com.tms.finalproject_autoshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private SpareParts product;
    private Integer quantity;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public Double getTotalPrice() {
        return product.getPrice() * quantity;
    }


}
