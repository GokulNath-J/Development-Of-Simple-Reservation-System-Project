package com.example.Booking.Service.Repository;

import com.example.Booking.Service.Entity.PassengerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerDetailsRepo extends JpaRepository<PassengerDetails,Integer> {
    PassengerDetails findByPnr(String pnr);
}
