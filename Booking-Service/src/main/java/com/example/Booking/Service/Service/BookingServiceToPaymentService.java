package com.example.Booking.Service.Service;

import com.example.Booking.Service.DTO.BookingRequest;
import com.example.Booking.Service.Entity.PremiumTatkalTickets;
import com.example.Booking.Service.Entity.TatkalTickets;
import com.example.Booking.Service.Feign.PaymentFeign;
import com.example.PaymentFailedException;
import feign.FeignException;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingServiceToPaymentService {

    private final static Logger log = LoggerFactory.getLogger(BookingServiceToPaymentService.class);

    @Autowired
    private PaymentFeign paymentFeign;


    @Autowired
    private EntityManager entityManager;

    //    @Transactional
    public void checkEntity(TatkalTickets ticket) {
        boolean isManaged = entityManager.contains(ticket);
        System.out.println("Is entity managed? " + isManaged);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public ResponseEntity<String> bookTatkalTicket(TatkalTickets tickets, BookingRequest request, double totalTicketAmount) throws PaymentFailedException {
        log.info("Request on BookingServiceToPaymentService");
//        int noOfTickets = request.getNumberOfTickets();
//        tickets.setNoOfSeatsBooked(tickets.getNoOfSeatsBooked() + noOfTickets);
//        tickets.setNoOfSeatsAvailable(tickets.getNoOfSeatsAvailable() - noOfTickets);
        checkEntity(tickets);
        log.info("userName:{}", request.getUserName());
        String paymentResult = "No Valid";
        try {
            paymentResult = paymentFeign.paymentRequest(request.getUserName(), totalTicketAmount).getBody();
            log.info("Payment Result in BookingServiceToPaymentService Try Block:{}", paymentResult);
            int noOfTickets = request.getNumberOfTickets();
            tickets.setNoOfSeatsBooked(tickets.getNoOfSeatsBooked() + noOfTickets);
            tickets.setNoOfSeatsAvailable(tickets.getNoOfSeatsAvailable() - noOfTickets);
            return ResponseEntity.ok(paymentResult);
        } catch (FeignException.BadRequest e) {
            String errorMessage = e.contentUTF8();
            errorMessage = errorMessage.replace("\"", "");
            System.out.println("Error message: " + errorMessage);
            throw new PaymentFailedException(errorMessage);
        } finally {
            log.info("Payment Result in BookingServiceToPaymentService finally:{}", paymentResult);
        }
    }

    public ResponseEntity<String> bookPremiumTatkalTicket(PremiumTatkalTickets premiumTatkalTickets, BookingRequest request, double totalTicketAmount) throws PaymentFailedException {
        log.info("Request on BookingServiceToPaymentService");
        int noOfTickets = request.getNumberOfTickets();
        premiumTatkalTickets.setNoOfSeatsAvailable(premiumTatkalTickets.getNoOfSeatsAvailable() - noOfTickets);
        premiumTatkalTickets.setNoOfSeatsBooked(premiumTatkalTickets.getNoOfSeatsBooked() + noOfTickets);
       // checkEntity(tickets);
        log.info("userName:{}", request.getUserName());
        String paymentResult = "No Valid";
        try {
            paymentResult = paymentFeign.paymentRequest(request.getUserName(), totalTicketAmount).getBody();
            log.info("Payment Result in BookingServiceToPaymentService Try Block:{}", paymentResult);
            return ResponseEntity.ok(paymentResult);
        } catch (FeignException.BadRequest e) {
            String errorMessage = e.contentUTF8();
            errorMessage = errorMessage.replace("\"", "");
            System.out.println("Error message: " + errorMessage);
            throw new PaymentFailedException(errorMessage);
        } finally {
            log.info("Payment Result in BookingServiceToPaymentService finally:{}", paymentResult);
        }
    }
}
