package com.tms.finalproject_autoshop.exception;


public class CartException extends Exception {
    public CartException() {
        super("Cart is empty!");
    }

    @Override
    public String toString() {
        return "Cart is empty!";
    }

}
