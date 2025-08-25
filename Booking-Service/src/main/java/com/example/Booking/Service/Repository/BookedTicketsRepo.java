package com.example.Booking.Service.Repository;

import com.example.Booking.Service.Entity.BookedTicketsAndStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookedTicketsRepo extends JpaRepository<BookedTicketsAndStatus,Integer> {

}
