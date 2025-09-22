package com.example.Booking.Service.Feign;

import com.example.Booking.Service.DTO.TrainCoachNumberDTO;
import com.example.Booking.Service.DTO.TrainDTOWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "Train-Service")
public interface TrainFeign {

    @GetMapping("/rail/sendTatkalAndPremiumTataklTicketsToBookingServiceManually")
    public TrainDTOWrapper sendTatkalAndPremiumTataklTicketsToBookingServiceManually();

    @GetMapping("/rail/sendNormalTicketsToBookingServiceManually")
    public TrainDTOWrapper sendNormalTicketsToBookingServiceManually();

    @GetMapping("/rail/sendTrainCoachNumberDTO")
    public List<TrainCoachNumberDTO> sendTrainCoachNumberDTO(@RequestParam Integer trainNumber);
}
