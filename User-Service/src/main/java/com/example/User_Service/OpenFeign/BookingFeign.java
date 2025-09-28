package com.example.User_Service.OpenFeign;

import com.example.PaymentFailedException;
import com.example.User_Service.DTO.BookingCancelRequestDTO;
import com.example.User_Service.DTO.BookingRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "Booking-Service")
public interface BookingFeign {

    @PostMapping("/booking/bookPremiumAndTatkal")
    public ResponseEntity<String> bookPremiumAndTatkal(@RequestBody BookingRequest request) throws PaymentFailedException;

    @PostMapping("booking/bookNormalReservation")
    public ResponseEntity<String> bookNormalReservation(@RequestBody BookingRequest request) throws PaymentFailedException;

    @PutMapping("/booking/bookingCancelRequest")
    public ResponseEntity<String> bookingCancelRequest(@RequestBody BookingCancelRequestDTO bookingCancelRequestDTO);
}
