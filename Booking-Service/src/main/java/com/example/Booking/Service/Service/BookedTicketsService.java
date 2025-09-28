package com.example.Booking.Service.Service;

import com.example.Booking.Service.DTO.BookingRequest;
import com.example.Booking.Service.DTO.BookingStatus;
import com.example.Booking.Service.DTO.PassengerDetailsDTO;
import com.example.Booking.Service.Entity.BookedTicketsAndStatus;
import com.example.Booking.Service.Entity.PassengerDetails;
import com.example.Booking.Service.Entity.TrainCoachNumberBooking;
import com.example.Booking.Service.Kafka.BookingEvent;
import com.example.Booking.Service.Repository.BookedTicketsRepo;
import com.example.Booking.Service.Repository.TrainCoachNumberBookingRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookedTicketsService {

    private final Logger log = LoggerFactory.getLogger(BookedTicketsService.class);

    @Autowired
    private BookedTicketsRepo bookedTicketsRepo;

    @Autowired
    private TrainCoachNumberBookingRepo trainCoachNumberBookingRepo;

    @Autowired
    private BookingEvent bookingEvent;


    //private List<TrainCoachNumberBooking> trainCoachNumberBookings = trainCoachNumberBookingRepo.findAll();
    @Transactional(propagation = Propagation.REQUIRED)
    public void addTickets(BookingRequest request, BookingStatus bookingStatus, String waitingToConfirmTicket, double totalTicketAmount, double eachTicketPrice, String transactionID) {
        log.info("Request in addTickets:{}", request);
        log.info("Booking Status:{}", bookingStatus);
        log.info("TotalTicketAmount:{}", totalTicketAmount);
        List<PassengerDetailsDTO> passengerDetailsDTO = request.getPassengersList();
        for (PassengerDetailsDTO detailsDTO : passengerDetailsDTO) {
            List<PassengerDetails> details = new ArrayList<>();
            BookedTicketsAndStatus bookedTicketsAndStatus = new BookedTicketsAndStatus(request, bookingStatus, eachTicketPrice);
            String pnr = UUID.randomUUID().toString().substring(0, 11).replace("-", "");
            bookedTicketsAndStatus.setPnr(pnr);
            bookedTicketsAndStatus.setTransactionID(transactionID);
            bookedTicketsAndStatus.setIsCancellingTicketsClosed(false);
            // PassengerDetails passengerDetails = new PassengerDetails(detailsDTO.getPassengerName(),detailsDTO.getGender(),detailsDTO.getAge());
            if (bookingStatus.equals(BookingStatus.CONFIRMED)) {
                PassengerDetails passengerDetails = addTrainCoachAndSeatNumber(bookedTicketsAndStatus, request);
                passengerDetails.setPassengerName(detailsDTO.getPassengerName());
                passengerDetails.setGender(detailsDTO.getGender());
                passengerDetails.setAge(detailsDTO.getAge());
                details.add(passengerDetails);
            } else {
                PassengerDetails passengerDetails = new PassengerDetails();
                passengerDetails.setCoachName(request.getCoachName());
                passengerDetails.setPassengerName(detailsDTO.getPassengerName());
                passengerDetails.setGender(detailsDTO.getGender());
                passengerDetails.setAge(detailsDTO.getAge());
                details.add(passengerDetails);
            }
            bookedTicketsAndStatus.setPassengersList(details);
            bookedTicketsRepo.save(bookedTicketsAndStatus);
            log.info("Saved BookedTicketsAndStatus:{}", bookedTicketsAndStatus);
        }
        return;
    }

    private PassengerDetails addTrainCoachAndSeatNumber(BookedTicketsAndStatus bookedTicketsAndStatus, BookingRequest request) {
        List<TrainCoachNumberBooking> trainCoachNumberBookingList = trainCoachNumberBookingRepo.findAllByTrainNumberAndCoachName(request.getTrainNumber(), request.getCoachName());
        PassengerDetails passengerDetails = new PassengerDetails();
        for (TrainCoachNumberBooking trainCoachNumberBooking : trainCoachNumberBookingList) {
            if (request.getNumberOfTickets() <= trainCoachNumberBooking.getTotalNoOfSeats()) {
                passengerDetails.setCoachNumber(trainCoachNumberBooking.getCoachNumber());
                passengerDetails.setCoachName(trainCoachNumberBooking.getCoachName());
                int seat = trainCoachNumberBooking.getSeats();
                passengerDetails.setSeatNumber(seat);
                trainCoachNumberBooking.setSeats(++seat);
            }
        }
        return passengerDetails;
    }

}
