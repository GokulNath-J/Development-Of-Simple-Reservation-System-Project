package com.example.Booking.Service.Feign;

import com.example.InsufficientBalanceException;
import com.example.PasswordIncorrectException;
import com.example.PaymentFailedException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "Payment-Service")
public interface PaymentFeign {

    @PostMapping("/payment/paymentRequest")
    public ResponseEntity<String> paymentRequest(@RequestParam String userName, @RequestParam double totalTicketAmount);
}
