package com.example.Booking.Service.Feign;

import com.example.Booking.Service.DTO.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@FeignClient(name = "Train-Service")
public interface TrainFeign {

    @GetMapping("/rail/sendTatkalAndPremiumTataklTicketsToBookingServiceManually")
    public List<PremiumAndTatkalDTO> sendTatkalAndPremiumTataklTicketsToBookingServiceManually();

    @GetMapping("/rail/sendNormalTicketsToBookingServiceManually")
    public TrainDTOWrapper sendNormalTicketsToBookingServiceManually();

    @GetMapping("/rail/sendTrainCoachNumberDTO")
    public List<TrainCoachNumberDTO> sendTrainCoachNumberDTO(@RequestParam Integer trainNumber);

    @PostMapping("/rail/getNextNormalReservationTickets")
    public NormalTicketDTOWrapper getNextNormalReservationTickets(@RequestBody Map<Integer, LocalDate> trainNumberAndLastTravelDay);

}
