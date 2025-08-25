package com.example;

public class PasswordIncorrectException extends Exception{

    public PasswordIncorrectException (String message) {
        super(message);
    }
}
