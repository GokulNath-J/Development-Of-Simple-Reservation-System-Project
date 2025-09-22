package com.example.Booking.Service.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PassengerDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String passengerName;
    private String gender;
    private int age;
    private String coachName;
    private String coachNumber;
    private int seatNumber;


    public PassengerDetails(String passengerName, String gender, int age) {
        this.passengerName = passengerName;
        this.gender = gender;
        this.age = age;
    }
}
