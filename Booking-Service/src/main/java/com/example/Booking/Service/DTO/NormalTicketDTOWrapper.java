package com.example.Booking.Service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Queue;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NormalTicketDTOWrapper {

    private Queue<NormalTicketDTO> normalTicketDTOQueue;
    private List<TrainCoachNumberDTO> trainCoachNumberDTOList;

}
