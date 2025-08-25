package com.example.Booking.Service.Entity;

import com.example.Booking.Service.DTO.BookingRequest;
import com.example.Booking.Service.DTO.BookingStatus;
import com.example.Booking.Service.DTO.PassengerDetailsDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookedTicketsAndStatus {

    @Id
    @SequenceGenerator(name = "bookedtickets", sequenceName = "seqbookedtickets", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookedtickets")
    private Integer id;
    private String userName;
    private Integer trainNumber;
    private LocalDate travelDate;
    private String coachName;
    private String fromStationName;
    private String toStationName;
    private Integer numberOfTickets;
    private String bookingMethod;
    private Double amount;

    @Enumerated(value = EnumType.STRING)
    private BookingStatus bookingStatus;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "userName",referencedColumnName = "userName")
    private List<PassengerDetails> passengersList;

    public BookedTicketsAndStatus(BookingRequest bookingRequest, BookingStatus bookingStatus, Double amount) {
        this.userName = bookingRequest.getUserName();
        this.trainNumber = bookingRequest.getTrainNumber();
        this.travelDate = bookingRequest.getTravelDate();
        this.coachName = bookingRequest.getCoachName();
        this.fromStationName = bookingRequest.getFromStationName();
        this.toStationName = bookingRequest.getToStationName();
        this.numberOfTickets = bookingRequest.getNumberOfTickets();
        this.bookingMethod = bookingRequest.getBookingMethod();
        this.amount = amount;
        this.bookingStatus = bookingStatus;

    }


}
