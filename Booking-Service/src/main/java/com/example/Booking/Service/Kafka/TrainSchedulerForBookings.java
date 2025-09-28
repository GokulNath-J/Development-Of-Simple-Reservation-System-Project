//package com.example.Booking.Service.Kafka;
//
//import com.example.Booking.Service.DTO.TrainDTOWrapper;
//import com.example.Booking.Service.Service.BookingService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TrainSchedulerForBookings {
//
//    @Autowired
//    private BookingService bookingService;
//
//    @KafkaListener(topics = "TrainDTO-Topic-1", groupId = "TrainDTO-Group-1")
//    public void receiveTrainDTO(TrainDTOWrapper trainDTOWrapper) {
//        bookingService.addTatkalAndPremiumTatkatTickets(trainDTOWrapper.getHashMap());
////        for (Map.Entry<Integer, List<PremiumAndTatkalDTO>> integerListEntry : dto1.entrySet()) {
////            System.out.println(integerListEntry.getKey());
////            System.out.println(integerListEntry.getValue());
////        }
//    }
//    @KafkaListener(topics = "TrainDTO-Topic-1",groupId = "TrainDTO-Group-1")
//    public void receiveMessage(String message){
//        System.out.println(message);
//    }
//
//
//    public void getNextNormalReservationTickets(){
//        bookingService.getNextNormalReservationTickets();
//    }
//
//}
