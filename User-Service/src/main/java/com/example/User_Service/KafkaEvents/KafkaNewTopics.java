package com.example.User_Service.KafkaEvents;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class KafkaNewTopics {


    @Bean
    public NewTopic bookingResponseTopic() {
        return new NewTopic("BookingResponse-Topic", 3, (short) 1);
    }
}
