package com.example.Train_Service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TrainStoppingStation {

    @Id
    @SequenceGenerator(name = "Seqstoppingstation", sequenceName = "seqtrainstoppingstation", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Seqstoppingstation")
    private Integer id;
    private Integer trainNumber;
    private String stationName;
    private Integer platformNo;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "trainNumber", referencedColumnName = "trainNumber")
    private List<TicketsPerStation> ticketsPerStations;
    


}


