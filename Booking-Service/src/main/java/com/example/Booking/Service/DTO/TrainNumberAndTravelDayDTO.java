package com.example.Booking.Service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainNumberAndTravelDayDTO {

    private Integer trainNumber;
    private LocalDate travelDay;
}
