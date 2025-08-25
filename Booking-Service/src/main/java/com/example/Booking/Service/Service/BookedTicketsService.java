package com.example.Booking.Service.Service;

import com.example.Booking.Service.DTO.BookingRequest;
import com.example.Booking.Service.DTO.BookingStatus;
import com.example.Booking.Service.DTO.PassengerDetailsDTO;
import com.example.Booking.Service.Entity.BookedTicketsAndStatus;
import com.example.Booking.Service.Entity.PassengerDetails;
import com.example.Booking.Service.Repository.BookedTicketsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookedTicketsService {

    private final Logger log = LoggerFactory.getLogger(BookedTicketsService.class);

    @Autowired
    private BookedTicketsRepo bookedTicketsRepo;

    public void addTickets(BookingRequest request, BookingStatus bookingStatus, double totalTicketAmount) {
        BookedTicketsAndStatus bookedTicketsAndStatus = new BookedTicketsAndStatus(request,bookingStatus,totalTicketAmount);
        List<PassengerDetailsDTO> passengerDetailsDTO = request.getPassengersList();
        List<PassengerDetails> details = new ArrayList<>();
        for (PassengerDetailsDTO detailsDTO : passengerDetailsDTO) {
            PassengerDetails passengerDetails = new PassengerDetails(detailsDTO.getPassengerName());
            details.add(passengerDetails);
        }
        bookedTicketsAndStatus.setPassengersList(details);
        bookedTicketsRepo.save(bookedTicketsAndStatus);
        log.info("Saved BookedTicketsAndStatus:{}", bookedTicketsAndStatus);
    }
}
