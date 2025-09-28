package com.example.Train_Service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NextDayTrainTickets {

    @Id
    @SequenceGenerator(name = "tickets", sequenceName = "seqtickets", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tickets")
    private Integer sNo;
    private Integer trainNumber;
    private String bookingType;
    private String coachName;
    private LocalDate travelDate;
    private LocalTime startingTime;
    private LocalDateTime arrivalDateTime;
    private LocalDateTime departureDateTime;
    private String stationName;
    private Integer totalNoOfSeats;
    private Double eachSeatPrice;

    public NextDayTrainTickets(Integer trainNumber, String bookingType, String coachName, LocalDate travelDate,
                               LocalTime startingTime, LocalDateTime arrivalDateTime,
                               LocalDateTime departureDateTime, String stationName, Integer totalNoOfSeats,
                               Double eachSeatPrice) {
        this.trainNumber = trainNumber;
        this.bookingType = bookingType;
        this.coachName = coachName;
        this.travelDate = travelDate;
        this.startingTime = startingTime;
        this.arrivalDateTime = arrivalDateTime;
        this.departureDateTime = departureDateTime;
        this.stationName = stationName;
        this.totalNoOfSeats = totalNoOfSeats;
        this.eachSeatPrice = eachSeatPrice;
    }
}
