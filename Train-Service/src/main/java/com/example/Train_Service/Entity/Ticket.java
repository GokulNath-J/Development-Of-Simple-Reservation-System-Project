package com.example.Train_Service.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    @Id
    @SequenceGenerator(name = "seqticket",sequenceName = "seqtrainticket",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seqticket")
    private int id;
    @Column(unique = true)
    private String ticketId;
    private Integer totalNoTickets;
    private Integer availableTickets;
    private Double eachSeatPrice;

}
