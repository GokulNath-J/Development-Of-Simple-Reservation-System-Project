package com.example.Booking.Service.Repository;


import com.example.Booking.Service.Entity.TrainCoachNumberBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainCoachNumberBookingRepo extends JpaRepository<TrainCoachNumberBooking,Integer> {
    List<TrainCoachNumberBooking> findAllByTrainNumberAndCoachName(Integer trainNumber, String coachName);
}
