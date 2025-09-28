package com.example.Booking.Service.Repository;

import com.example.Booking.Service.DTO.BookingStatus;
import com.example.Booking.Service.Entity.BookedTicketsAndStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookedTicketsRepo extends JpaRepository<BookedTicketsAndStatus,Integer> {

//    List<BookedTicketsAndStatus> findByAllBookingStatus(BookingStatus bookingStatus);

    List<BookedTicketsAndStatus> findAllByBookingStatusAndTravelDate(BookingStatus bookingStatus, LocalDate localDate);

    BookedTicketsAndStatus findByPnr(String pnr);

    List<BookedTicketsAndStatus> findAllByTrainNumberAndTravelDate(Integer trainNumber, LocalDate travelDate);
}
