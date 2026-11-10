package com.example.sample.exception;

public class PasswordTooLongException extends RuntimeException {
    public PasswordTooLongException(String message){
        super(message);
    }
}
