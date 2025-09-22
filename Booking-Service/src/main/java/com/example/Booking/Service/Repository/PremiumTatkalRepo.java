package com.example.Booking.Service.Repository;

import com.example.Booking.Service.Entity.PremiumTatkalTickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PremiumTatkalRepo extends JpaRepository<PremiumTatkalTickets,Integer> {
    List<PremiumTatkalTickets> findAllByTrainNumber(Integer trainNumber);
}
