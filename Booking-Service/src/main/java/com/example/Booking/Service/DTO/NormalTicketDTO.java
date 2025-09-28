package com.example.Booking.Service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NormalTicketDTO {

    private Integer train_number;
    private String booking_type;
    private LocalDate travelDate;
    private LocalTime startingTime;
    private LocalDateTime arrivalDateTime;
    private LocalDateTime departureDateTime;
    private String station_name;
    private String coach_name;
    private Integer total_no_of_seats;
    private Double each_seat_price;
}
