package com.example.Train_Service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainCoachNumber {

    @Id
    @SequenceGenerator(name = "seqcoachesnum", sequenceName = "seqtrainCoachesNumber", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqcoachesnum")
    private int id;
    private Integer trainNumber;
    private String coachName;
    private String coachNumber;

}
