package com.example.Train_Service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TrainCoachNumberDTO {

    private Integer trainNumber;
    private String coachName;
    private Integer totalNoOfSeats;
    private List<String> coachNumber;
}
