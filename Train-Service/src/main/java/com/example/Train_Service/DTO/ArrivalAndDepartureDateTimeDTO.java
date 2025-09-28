package com.example.Train_Service.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ArrivalAndDepartureDateTimeDTO {

    private LocalDate travlDate;
    private LocalDateTime arrivalDateTime;
    private LocalDateTime departureDateTime;
}
