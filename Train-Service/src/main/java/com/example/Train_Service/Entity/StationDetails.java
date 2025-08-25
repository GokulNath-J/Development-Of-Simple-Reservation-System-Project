package com.example.Train_Service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StationDetails {

    @Id
    @SequenceGenerator(name = "seqstation",sequenceName = "seqstationdetails",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seqstation")
    private Integer id;
    private String stationCode;
    private String stationName;
    private Integer noOfPlatforms;


}
