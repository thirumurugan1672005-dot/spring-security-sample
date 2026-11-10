package com.example.sample.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message){
         super(message);
    }
}
