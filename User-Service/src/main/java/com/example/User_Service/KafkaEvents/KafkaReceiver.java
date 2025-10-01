package com.example.User_Service.KafkaEvents;


import com.example.User_Service.DTO.BookingResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaReceiver {


    @KafkaListener(topics = "BookingResponse-Topic", groupId = "BookingResponse-Group-1")
    public void receiveMessage(BookingResponse bookingResponse) {
        System.out.println("Booking Received In method:"+bookingResponse);
    }


}
