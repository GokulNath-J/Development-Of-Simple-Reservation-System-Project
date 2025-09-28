package com.example.Train_Service.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TrainRunningDays {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String day;

}
