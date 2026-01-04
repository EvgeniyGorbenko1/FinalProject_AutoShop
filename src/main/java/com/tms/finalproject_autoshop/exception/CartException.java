package com.tms.finalproject_autoshop.exception;

import com.tms.finalproject_autoshop.model.Cart;

public class CartException extends Exception{
    private Cart cart;

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public CartException() {
        super("Cart is empty!");
    }

    @Override
    public String toString() {
        return "Cart is empty!";
    }

}
