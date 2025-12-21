package com.tms.finalproject_autoshop.exception;

import lombok.Data;

public class UsernameUsedException extends Exception {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UsernameUsedException(String username) {
        super("Username already used!");
    }

    @Override
    public String toString() {
        return "username already used! " + username;
    }
}
