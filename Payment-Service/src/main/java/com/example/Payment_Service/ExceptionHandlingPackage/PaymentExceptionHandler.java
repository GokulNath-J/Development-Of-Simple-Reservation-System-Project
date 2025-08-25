package com.example.Payment_Service.ExceptionHandlingPackage;

import com.example.InsufficientBalanceException;
import com.example.PasswordIncorrectException;
import com.example.PaymentFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PaymentExceptionHandler {

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<String> handleInsufficientBalance(InsufficientBalanceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<String> handlePaymentFailed(PaymentFailedException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(PasswordIncorrectException.class)
    public ResponseEntity<String> handlePasswordIncorrect(PasswordIncorrectException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}