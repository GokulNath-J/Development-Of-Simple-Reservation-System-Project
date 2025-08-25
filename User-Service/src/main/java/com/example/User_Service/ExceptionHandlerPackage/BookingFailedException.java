package com.example.User_Service.ExceptionHandlerPackage;

public class BookingFailedException extends RuntimeException{
    public BookingFailedException(String message) {
        super(message);
    }
}
