package com.example.Train_Service.DTO;

import com.example.Train_Service.Entity.TrainCoachNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TrainDTO1 {
    private Integer train_number;
    private String booking_type;
    private String station_name;
    private String coach_name;
    private Integer total_no_of_seats;
    private Double each_seat_price;

}
