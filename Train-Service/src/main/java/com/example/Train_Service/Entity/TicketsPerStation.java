package com.example.Train_Service.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketsPerStation {

    @Id
    @SequenceGenerator(name = "ticketsperstation", sequenceName = "seqticketsperstation", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticketsperstation")
    private Integer sNo;
    private Integer trainNumber;
    private String stationName;
    private String bookingType;
    private String coachName;
    private Integer totalNoOfSeats;
    private Double eachSeatPrice;

    public TicketsPerStation(Integer trainNumber, String stationName, String bookingType, String coachName, Integer totalNoOfSeats, Double eachSeatPrice) {
        this.trainNumber = trainNumber;
        this.stationName = stationName;
        this.bookingType = bookingType;
        this.coachName = coachName;
        this.totalNoOfSeats = totalNoOfSeats;
        this.eachSeatPrice = eachSeatPrice;
    }

    public TicketsPerStation(String stationName, String bookingType, String coachName, Integer totalNoOfSeats, Double eachSeatPrice) {
        this.stationName = stationName;
        this.bookingType = bookingType;
        this.coachName = coachName;
        this.totalNoOfSeats = totalNoOfSeats;
        this.eachSeatPrice = eachSeatPrice;
    }
}
