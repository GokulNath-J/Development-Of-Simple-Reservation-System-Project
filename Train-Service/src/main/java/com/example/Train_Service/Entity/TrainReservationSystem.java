package com.example.Train_Service.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainReservationSystem {

    @Id
    @SequenceGenerator(name = "trainreservationseq",sequenceName = "seqtrainreservation", allocationSize = 1,initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "trainreservationseq")
    private Integer id;
    private Integer trainNumber;
    private Integer noAcCoachTickets;
    private Integer noSleeperCoachTickets;
    private Integer noNonAcReservationTickets;
    private Integer totalNoReservationTickets;
    private LocalDateTime reservationOpensAt;
    private LocalDateTime reservationClosesAt;
}
