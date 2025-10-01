package com.example.Booking.Service.Kafka;

import com.example.Booking.Service.DTO.BookingResponse;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BookingEvent {


    private final KafkaTemplate<String, BookingResponse> sendbookingResponse;

    public void sendBookingResponseToUser(BookingResponse bookingResponse) {
        System.out.println("BookingResponse Send to User");
        sendbookingResponse.send("BookingResponse-Topic", bookingResponse);
    }
}
