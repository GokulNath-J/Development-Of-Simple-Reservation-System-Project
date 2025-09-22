package com.example.Train_Service.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainCoaches {

    @Id
    @SequenceGenerator(name = "seqcoaches", sequenceName = "seqtrainCoaches", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqcoaches")
    private int id;
    private Integer trainNumber;
    private String coachName;
    private Integer totalNoOfCoaches;
    private Integer totalNoSeats;
    private Double eachSeatPrice;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "coachId")
    private List<TrainCoachNumber> trainCoachNumberList;


}

