package com.example.Booking.Service.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PremiumTatkalTickets {

    @Id
    @SequenceGenerator(name = "prtickets", sequenceName = "seqprtickets", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prtickets")
    private Integer sNo;
    private Integer trainNumber;
    private String coachName;
    private String stationName;
    private String bookingType;
    private Integer totalNoOfSeats;
    private Integer noOfSeatsAvailable;
    private Integer noOfSeatsBooked;
    private Double eachSeatPrice;


    public PremiumTatkalTickets(Integer trainNumber, String coachName, String stationName, String bookingType, Integer totalNoOfSeats, Integer noOfSeatsAvailable, Integer noOfSeatsBooked, Double eachSeatPrice) {
        this.trainNumber = trainNumber;
        this.coachName = coachName;
        this.stationName = stationName;
        this.bookingType = bookingType;
        this.totalNoOfSeats = totalNoOfSeats;
        this.noOfSeatsAvailable = noOfSeatsAvailable;
        this.noOfSeatsBooked = noOfSeatsBooked;
        this.eachSeatPrice = eachSeatPrice;
    }
}
