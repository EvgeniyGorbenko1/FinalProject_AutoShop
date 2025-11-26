package com.tms.finalproject_autoshop.exception;

public class EmailException extends Exception {
    String email;

    public EmailException(String email){
        super("Email already exists");
    }

    @Override
    public String toString() {
        return "Email is already exists" + email;
    }
}
