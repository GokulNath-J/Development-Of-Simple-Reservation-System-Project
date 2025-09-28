package com.example.Train_Service.Repository;

import com.example.Train_Service.Entity.NextDayTrainTickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NextDayTrainTicketsRepo extends JpaRepository<NextDayTrainTickets, Integer> {
    List<NextDayTrainTickets> findAllByTravelDate(LocalDate now);

    List<NextDayTrainTickets> findAllByTrainNumberAndTravelDate(Integer key, LocalDate value);
}
