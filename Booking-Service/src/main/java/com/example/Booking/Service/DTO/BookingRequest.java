package com.example.Booking.Service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookingRequest {


    private String userName;
    private Integer trainNumber;
    private LocalDate travelDate; // yyyy-MM-dd
    private String coachName;
    private String fromStationName;
    private String toStationName;
    private Integer numberOfTickets;
    private List<PassengerDetailsDTO> passengersList;
    private String bookingMethod; // Tatkal, Premium Tatkal, Normal
//    private double amount; // Final amount (only in confirm step)
}