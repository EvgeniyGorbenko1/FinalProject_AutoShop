package com.tms.finalproject_autoshop.exception;

public class EmailException extends Exception {
    String email;

    @Override
    public String toString() {
        return "Email is already exists" + email;
    }
}
