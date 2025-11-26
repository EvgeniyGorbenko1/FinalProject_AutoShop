package com.tms.finalproject_autoshop.exception;

public class AgeException  extends Exception{
    int age;

    public AgeException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "Age incorrect!";
    }
}
