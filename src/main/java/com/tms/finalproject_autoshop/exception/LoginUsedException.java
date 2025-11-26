package com.tms.finalproject_autoshop.exception;

public class LoginUsedException extends Exception {
    String login;

    public LoginUsedException(String login) {
        super("Login already used!");
    }

    @Override
    public String toString() {
        return "Login already used! " + login;
    }
}
