package com.example.Train_Service.Feign;


import com.example.Train_Service.DTO.NormalTicketDTOWrapper;
import com.example.Train_Service.DTO.TrainCoachNumberDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "Booking-Service")
public interface BookingServiceFeign {

    @PostMapping("/booking/sendNormalTickets")
    public void sendNormalReservationTickets(@RequestBody NormalTicketDTOWrapper normalTicketDTOWrapper);
}
