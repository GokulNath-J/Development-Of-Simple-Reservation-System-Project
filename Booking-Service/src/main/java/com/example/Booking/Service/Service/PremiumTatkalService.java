package com.example.Booking.Service.Service;

import com.example.Booking.Service.DTO.BookingRequest;
import com.example.Booking.Service.DTO.BookingStatus;
import com.example.Booking.Service.DTO.TicketPrice;
import com.example.Booking.Service.Entity.PremiumTatkalTickets;
import com.example.Booking.Service.Entity.TatkalTickets;
import com.example.Booking.Service.Repository.PremiumTatkalRepo;
import com.example.Booking.Service.Repository.TicketPriceRepo;
import com.example.PaymentFailedException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

@Service
public class PremiumTatkalService {

    private final static Logger log = LoggerFactory.getLogger(PremiumTatkalService.class);

    private static final LocalTime tatkal_opens_at_for_nonsleepers = LocalTime.of(10, 00, 00);
    private static final LocalTime tatkal_opens_at_for_sleepers = LocalTime.of(11, 00, 00);


    @Autowired
    private PremiumTatkalRepo premiumTatkalRepo;

    @Autowired
    private TicketPriceRepo ticketPriceRepo;

    @Autowired
    private BookingServiceToPaymentService bookingServiceToPaymentService;

    @Autowired
    private BookedTicketsService bookedTicketsService;

    @Transactional
    public ResponseEntity<String> book(BookingRequest request) throws PaymentFailedException {
        log.info("Request in TatkalService");
        log.info("BookingRequest Before Travel Date Check:{}", request);
        if (request.getTravelDate().equals(LocalDate.now().plusDays(1))) {
            log.info("getTravelDate() is Matched:");
            List<PremiumTatkalTickets> premiumtatkalTicketslist = premiumTatkalRepo.findAllByTrainNumber(request.getTrainNumber());
            log.info("TatkalTickets in TatkalService:{}", premiumtatkalTicketslist);
            PremiumTatkalTickets premiumTatkalTickets = new PremiumTatkalTickets();
            for (PremiumTatkalTickets premiumTatkalTickets1 : premiumtatkalTicketslist) {
                log.info("Inside For-each Loop");
                if (premiumTatkalTickets1.getStationName().equalsIgnoreCase(request.getFromStationName())
                        && premiumTatkalTickets1.getCoachName().equalsIgnoreCase(request.getCoachName())) {
                    log.info("Tatkel Ticket Found {}", true);
                    premiumTatkalTickets = premiumTatkalTickets1;
                }
            }
            log.info("TatkalTickets:{}", premiumTatkalTickets);
            // checkEntity(tickets);
            double totalTicketAmount = 0;
            if (request.getNumberOfTickets() <= premiumTatkalTickets.getNoOfSeatsAvailable()) {
                log.info("Tickets Are Available");
                totalTicketAmount = calculateTotalAmount(request.getBookingMethod(), premiumTatkalTickets.getEachSeatPrice(),
                        request.getNumberOfTickets(), request.getCoachName());
                if (totalTicketAmount > 0.0) {
                    String result = bookingServiceToPaymentService.bookPremiumTatkalTicket(premiumTatkalTickets, request, totalTicketAmount).getBody();
                    log.info("Payment Result in TatkalService:{}", result);
                    if (result.equalsIgnoreCase("Payment Success")) {
                        addTicketsToAnotherStations(premiumtatkalTicketslist, request.getToStationName(), request.getCoachName(), request.getNumberOfTickets());
                        premiumTatkalTickets.setNoOfSeatsBooked(premiumTatkalTickets.getNoOfSeatsBooked() + request.getNumberOfTickets());
                        bookedTicketsService.addTickets(request, BookingStatus.CONFIRMED, totalTicketAmount);
                        return ResponseEntity.ok("Ticket Booked Successfully");
                    } else {
                        return ResponseEntity.badRequest().body(result);
                    }
                } else {
                    return ResponseEntity.ok("Booking Cancelled");
                }
            } else {
                System.out.println("Tickets are Insufficient We Can Confirm Onces Tickets Available");
                System.out.print("Do you Want to proceed:Y/N:");
                Scanner scanner = new Scanner(System.in);
                String yesOrno = scanner.nextLine();
                if (yesOrno.equalsIgnoreCase("y")) {
                    String result = bookingServiceToPaymentService.bookPremiumTatkalTicket(premiumTatkalTickets, request, totalTicketAmount).getBody();
                    log.info("Payment Result in TatkalService:{}", result);
                    if (result.equalsIgnoreCase("Payment Success")) {
                        bookedTicketsService.addTickets(request, BookingStatus.WAITING, totalTicketAmount);
                        return ResponseEntity.ok("Ticket Booked Successfully In the WAITING List");
                    } else {
                        return ResponseEntity.badRequest().body(result);
                    }
                } else {
                    return ResponseEntity.ok("Booking Cancelled");
                }
            }
        }
        return ResponseEntity.ok("There is Not Train on this Date");
    }

    private double calculateTotalAmount(String bookingMethod, Double eachSeatPrice, int numberOfTickets, String coachName) {
        log.info("Request in calculateTotalAmount Method in TatkalService");
        TicketPrice ticketPrice = ticketPriceRepo.findByBookingTypeAndCoachName(bookingMethod, coachName);
        Scanner scanner = new Scanner(System.in);
        if (bookingMethod.equalsIgnoreCase("Tatkal")) {
            double totalTicketPrice = (ticketPrice.getPrice() + eachSeatPrice) * numberOfTickets;
            System.out.println("Total Tatakl TicketsPrice = " + totalTicketPrice);
            System.out.print("Do yo want to place booking Y/N.?:");
            String yesOrno = scanner.nextLine();
            if (yesOrno.equalsIgnoreCase("y")) {
                return totalTicketPrice;
            }
        }
        return 0.0;
    }

    private void addTicketsToAnotherStations(List<PremiumTatkalTickets> premiumTatkalTickets, String toStationName, String coachName, Integer numberOfTickets) {
        log.info("Adding Tickets To Others Station:{},{},{}", toStationName, coachName, numberOfTickets);
        PremiumTatkalTickets tickets = null;
        for (PremiumTatkalTickets premiumTatkalTickets1 : premiumTatkalTickets) {
            if (premiumTatkalTickets1.getStationName().equalsIgnoreCase(toStationName)
                    && premiumTatkalTickets1.getCoachName().equalsIgnoreCase(coachName)) {
                tickets = premiumTatkalTickets1;
            }
        }
        tickets.setNoOfSeatsAvailable(tickets.getNoOfSeatsAvailable() + numberOfTickets);
        log.info("Tickets Successfully Added to Station:{}", toStationName);
    }
}



