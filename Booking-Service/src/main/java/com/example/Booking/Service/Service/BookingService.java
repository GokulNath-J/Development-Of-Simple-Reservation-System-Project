package com.example.Booking.Service.Service;


import com.example.Booking.Service.DTO.*;
import com.example.Booking.Service.Entity.*;
import com.example.Booking.Service.Feign.TrainFeign;
import com.example.Booking.Service.Kafka.BookingEvent;
import com.example.Booking.Service.Repository.*;
import com.example.PaymentFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class BookingService {

    private final static Logger log = LoggerFactory.getLogger(BookingService.class);
    private static boolean isTatkalAndPremiunTatkalClosed = true;
    private static LocalTime normalReservationClosingTime = LocalTime.of(20, 0, 0);
    private final TatkalRepo tatkalRepo;
    private final PremiumTatkalRepo premiumTatkalRepo;
    private final NormalReservationRepo normalReservationRepo;
    @Autowired
    private BookingEvent bookingEvent;
    @Autowired
    private TicketPriceRepo ticketPriceRepo;
    //    @Autowired
//    private Booking booking;
    @Autowired
    private TrainFeign trainFeign;
    @Autowired
    private TatkalService tatkalService;
    @Autowired
    private PremiumTatkalService premiumTatkalService;
    @Autowired
    private TrainCoachNumberBookingRepo trainCoachNumberBookingRepo;
    @Autowired
    private NormalReservationService normalReservationService;
    @Autowired
    private BookedTicketsRepo bookedTicketsRepo;
    @Autowired
    private BookingServiceToPaymentService bookingServiceToPaymentService;
    @Autowired
    private PassengerDetailsRepo passengerDetailsRepo;

    private Map<Integer, LocalDate> trainNumberAndLastTravelDate = new HashMap<>();

    private Map<Integer, TrainNumberTravelDateStartingTime> trainNumberTravelDateStartingTimes = new HashMap<>();

    @Autowired
    public BookingService(TatkalRepo tatkalRepo, PremiumTatkalRepo premiumTatkalRepo, NormalReservationRepo normalReservationRepo) {
        this.tatkalRepo = tatkalRepo;
        this.premiumTatkalRepo = premiumTatkalRepo;
        this.normalReservationRepo = normalReservationRepo;
    }

    public static boolean isIsTatkalAndPremiunTatkalClosed() {
        return isTatkalAndPremiunTatkalClosed;
    }

    public static void setIsTatkalAndPremiunTatkalClosed(boolean isTatkalAndPremiunTatkalClosed) {
        BookingService.isTatkalAndPremiunTatkalClosed = isTatkalAndPremiunTatkalClosed;
    }

    public Map<Integer, TrainNumberTravelDateStartingTime> getTrainNumberTravelDateStartingTimes() {
        return trainNumberTravelDateStartingTimes;
    }

    public void setTrainNumberTravelDateStartingTimes(Map<Integer, TrainNumberTravelDateStartingTime> trainNumberTravelDateStartingTimes) {
        this.trainNumberTravelDateStartingTimes = trainNumberTravelDateStartingTimes;
    }

    //--------------------------------------------------------------------------------------------------------------------

    public List<PremiumAndTatkalDTO> getPremiumAndTataklDTOManually() {
        List<PremiumAndTatkalDTO> trainDTO1 = trainFeign.sendTatkalAndPremiumTataklTicketsToBookingServiceManually();
        addTatkalAndPremiumTatkatTickets(trainDTO1);
        return trainDTO1;
    }

    public void addTatkalAndPremiumTatkatTickets(List<PremiumAndTatkalDTO> trainDTO1) {
        List<TatkalTickets> tatkalList = new ArrayList<>();
        List<PremiumTatkalTickets> premiunTataklList = new ArrayList<>();
        for (PremiumAndTatkalDTO dto1 : trainDTO1) {
            if (dto1.getBooking_type().equalsIgnoreCase("Premium Tatkal")) {
                PremiumTatkalTickets premiumTatkalTickets = new PremiumTatkalTickets(dto1.getTrain_number(), dto1.getCoach_name(), dto1.getStation_name(), dto1.getBooking_type(), dto1.getTotal_no_of_seats(), dto1.getTotal_no_of_seats(), 0, dto1.getEach_seat_price());
                premiunTataklList.add(premiumTatkalTickets);
            } else {
                TatkalTickets tatkalTickets = new TatkalTickets(dto1.getTrain_number(), dto1.getCoach_name(), dto1.getStation_name(), dto1.getBooking_type(), dto1.getTotal_no_of_seats(), dto1.getTotal_no_of_seats(), 0, dto1.getEach_seat_price());
                tatkalList.add(tatkalTickets);
            }
        }
        tatkalRepo.saveAll(tatkalList);
        premiumTatkalRepo.saveAll(premiunTataklList);
    }

    public ResponseEntity<String> bookPremiumAndTatkal(BookingRequest request) throws PaymentFailedException {
        log.info("request in the BookingService");
        if ((isTatkalAndPremiunTatkalClosed != true) && request.getBookingMethod().equalsIgnoreCase("Tatkal")) {
            return tatkalService.book(request);
        } else if ((isTatkalAndPremiunTatkalClosed != true) && request.getBookingMethod().equalsIgnoreCase("Premium Tatkal")) {
            return premiumTatkalService.book(request);
        }
        return ResponseEntity.badRequest().body("Booking Not Yet Openned Or Select the right BookingRequest");
    }

    public String getTrainCoachNumberDTOManually(Integer trainNumber) {
        List<TrainCoachNumberDTO> list = trainFeign.sendTrainCoachNumberDTO(trainNumber);
        for (TrainCoachNumberDTO trainCoachNumberDTO : list) {
            List<String> stringList = trainCoachNumberDTO.getCoachNumber();
            for (String string : stringList) {
                TrainCoachNumberBooking trainCoachNumberBooking = new TrainCoachNumberBooking(trainCoachNumberDTO.getTrainNumber(), trainCoachNumberDTO.getCoachName(), trainCoachNumberDTO.getTotalNoOfSeats());
                trainCoachNumberBooking.setCoachNumber(string);
                trainCoachNumberBookingRepo.save(trainCoachNumberBooking);
            }
        }
        return "Success";
    }

    public List<TatkalTickets> getAllTatkalTickets() {
        return tatkalRepo.findAll();
    }

    public List<PremiumTatkalTickets> getPremiumTatkalTickets() {
        return premiumTatkalRepo.findAll();
    }

    public List<TatkalTickets> getAllTatkalTicketsByTrainNumber(Integer trainNumber) {
        return tatkalRepo.findAllByTrainNumber(trainNumber);
    }

    public void addPrice(TicketPrice ticketPrice) {
        ticketPriceRepo.save(ticketPrice);
    }


//    public HashMap<Integer, List<PremiumAndTatkalDTO>> getNormalTicketsManually() {
//        HashMap<Integer, List<PremiumAndTatkalDTO>> trainDTO1 = trainFeign.sendNormalTicketsToBookingServiceManually().getHashMap();
//        addNormalTickets(trainDTO1);
//        return trainDTO1;
//    }

    //    private void addNormalTickets(HashMap<Integer, List<PremiumAndTatkalDTO>> trainDTO1) {
//        List<NormalReservationTickets> normalReservationTicketsList = new ArrayList<>();
//        for (Map.Entry<Integer, List<PremiumAndTatkalDTO>> listEntry : trainDTO1.entrySet()) {
//            List<PremiumAndTatkalDTO> dto1List = listEntry.getValue();
//            for (PremiumAndTatkalDTO dto1 : dto1List) {
//                NormalReservationTickets normalReservationTickets = new NormalReservationTickets
//                        (dto1.getTrain_number(), dto1.getCoach_name(), dto1.getArrivalDateTime(), dto1.getTravelDay(),
//                                dto1.getStation_name(), dto1.getBooking_type(), dto1.getTotal_no_of_seats(),
//                                dto1.getTotal_no_of_seats(), 0, dto1.getEach_seat_price());
//                normalReservationTicketsList.add(normalReservationTickets);
//            }
//        }
//        normalReservationRepo.saveAll(normalReservationTicketsList);
//    }
    public ResponseEntity<String> bookNormalReservation(BookingRequest request) throws PaymentFailedException {
        for (Map.Entry<Integer, TrainNumberTravelDateStartingTime> entry : trainNumberTravelDateStartingTimes.entrySet()) {
            TrainNumberTravelDateStartingTime time = entry.getValue();
            if (request.getTrainNumber().equals(entry.getKey())) {
                if (request.getTravelDate().equals(time.getTravelDate())) {
                    if (!time.isBookingClosed()) {
                        return normalReservationService.bookNormalReservationTickets(request);
                    } else {
                        log.info("Booking Closed");
                        return ResponseEntity.badRequest().body("Booking Closed");
                    }
                } else {
                    return normalReservationService.bookNormalReservationTickets(request);
                }
            }
        }
        return ResponseEntity.badRequest().body("Enter the Correct Train Number");
    }


    public ResponseEntity<String> getNextNormalReservationTickets() {
        log.info("Request in getNextNormalReservationTickets");
        NormalTicketDTOWrapper wrapper = trainFeign.getNextNormalReservationTickets(trainNumberAndLastTravelDate);
        Queue<NormalTicketDTO> dtos = wrapper.getNormalTicketDTOQueue();
        log.info("dtos:{}", dtos);
        List<NormalReservationTickets> ticketsList = new ArrayList<>();
        for (NormalTicketDTO dto : dtos) {
            trainNumberAndLastTravelDate.put(dto.getTrain_number(), dto.getTravelDate());
            NormalReservationTickets normalReservationTickets = new NormalReservationTickets
                    (dto.getTrain_number(), dto.getBooking_type(), dto.getCoach_name(), dto.getTravelDate(),
                            dto.getArrivalDateTime(), dto.getDepartureDateTime(), dto.getStation_name(),
                            dto.getTotal_no_of_seats(), dto.getTotal_no_of_seats(), 0,
                            dto.getEach_seat_price(), dto.getStartingTime());

            ticketsList.add(normalReservationTickets);
        }
        normalReservationRepo.saveAll(ticketsList);
        return ResponseEntity.ok("Next Normal Reservation Tickets Saved");
    }

    public void addNormalReservationTickets(NormalTicketDTOWrapper normalTicketDTOWrapper) {
        Queue<NormalTicketDTO> normalTicketDTO = normalTicketDTOWrapper.getNormalTicketDTOQueue();
        Queue<NormalReservationTickets> reservationTickets = new LinkedList<>();
        Queue<NormalReservationTickets> reservationTicketsQueue = NormalReservationTickets.getNormalReservationTicketsQueue();
//        Set<Integer> trainNumnuberSet = new HashSet<>();
        for (NormalTicketDTO dto : normalTicketDTO) {
            Integer trainNumber = dto.getTrain_number();
            LocalDate travelDate = dto.getTravelDate();
            NormalReservationTickets normalReservationTickets = new NormalReservationTickets
                    (trainNumber, dto.getBooking_type(), dto.getCoach_name(), travelDate,
                            dto.getArrivalDateTime(), dto.getDepartureDateTime(), dto.getStation_name(),
                            dto.getTotal_no_of_seats(), dto.getTotal_no_of_seats(), 0,
                            dto.getEach_seat_price(), dto.getStartingTime());
            reservationTickets.add(normalReservationTickets);
            reservationTicketsQueue.add(normalReservationTickets);
            trainNumberAndLastTravelDate.put(trainNumber, travelDate);
        }
        normalReservationRepo.saveAll(reservationTickets);
        getTrainCoachNumberDTO(normalTicketDTOWrapper.getTrainCoachNumberDTOList());
    }

    private void getTrainCoachNumberDTO(List<TrainCoachNumberDTO> trainCoachNumberDTOS) {
        for (TrainCoachNumberDTO trainCoachNumberDTO : trainCoachNumberDTOS) {
            List<String> stringList = trainCoachNumberDTO.getCoachNumber();
            for (String string : stringList) {
                TrainCoachNumberBooking trainCoachNumberBooking = new TrainCoachNumberBooking(trainCoachNumberDTO.getTrainNumber(), trainCoachNumberDTO.getCoachName(), trainCoachNumberDTO.getTotalNoOfSeats());
                trainCoachNumberBooking.setCoachNumber(string);
                trainCoachNumberBookingRepo.save(trainCoachNumberBooking);
            }
        }
    }

    public List<NormalReservationTickets> getTrainByTrainNumber(Integer trainNumber) {
        return normalReservationRepo.findAllByTrainNumber(trainNumber);
    }

    @Transactional
    public void getWaitingListTickets() {
        log.info("Request in getWaitingListTickets()");
        List<BookedTicketsAndStatus> list = bookedTicketsRepo.findAllByBookingStatusAndTravelDate(BookingStatus.WAITING, LocalDate.now().plusDays(1));
        log.info("BookedTicketsAndStatus in getWaitingListTickets():{}", list);
        Map<Integer, List<BookedTicketsAndStatus>> map = new HashMap<>();
        Map<Integer, List<NormalReservationTickets>> NR = new HashMap<>();
        Map<Integer, List<PremiumTatkalTickets>> PR = new HashMap<>();
        Map<Integer, List<TatkalTickets>> TT = new HashMap<>();
        LocalDate travelDate = list.getFirst().getTravelDate();
        for (BookedTicketsAndStatus bookedTicketsAndStatus : list) {
            Integer trainNumber = bookedTicketsAndStatus.getTrainNumber();
            if (map.containsKey(trainNumber)) {
                map.get(trainNumber).add(bookedTicketsAndStatus);
            } else {
                List<BookedTicketsAndStatus> newlist = new ArrayList<>();
                newlist.add(bookedTicketsAndStatus);
                map.put(trainNumber, newlist);
            }
            if (map.containsKey(trainNumber)) {
                map.get(trainNumber).add(bookedTicketsAndStatus);
            } else {
                List<BookedTicketsAndStatus> newlist = new ArrayList<>();
                newlist.add(bookedTicketsAndStatus);
                map.put(trainNumber, newlist);
            }
            if (map.containsKey(trainNumber)) {
                map.get(trainNumber).add(bookedTicketsAndStatus);
            } else {
                List<BookedTicketsAndStatus> newlist = new ArrayList<>();
                newlist.add(bookedTicketsAndStatus);
                map.put(trainNumber, newlist);
            }
        }
        if (map.size() != 0) {
            log.info("(map.size():{}", map.size());
            getAvailableTicketsFromDB(map, travelDate);
        }
    }

    @Transactional
    private void getAvailableTicketsFromDB(Map<Integer, List<BookedTicketsAndStatus>> map, LocalDate travelDate) {
        log.info("Request in getAvailableTicketsFromDB");
//        List<NormalReservationTickets> normalTickets = new ArrayList<>();
        Map<Integer, List<PremiumTatkalTickets>> PT = new HashMap<>();
        Map<Integer, List<TatkalTickets>> TT = new HashMap<>();
        Map<Integer, List<NormalReservationTickets>> NR = new HashMap<>();
        for (Integer trainNumber : map.keySet()) {
            List<PremiumTatkalTickets> ticketsList = premiumTatkalRepo.findAllByTrainNumber(trainNumber);
            PT.put(trainNumber, ticketsList);
        }
        for (Integer trainNumber : map.keySet()) {
            List<TatkalTickets> ticketsList = tatkalRepo.findAllByTrainNumber(trainNumber);
            TT.put(trainNumber, ticketsList);
        }
        for (Integer trainNumber : map.keySet()) {
            List<NormalReservationTickets> ticketsList = normalReservationRepo.findAllByTrainNumberAndTravelDate(trainNumber, travelDate);
            NR.put(trainNumber, ticketsList);
        }
        log.info("PT:{}", PT);
        log.info("TT:{}", TT);
        log.info("NR:{}", NR);
        log.info("PT Size:{}", PT.size());
        log.info("TT Size:{}", TT.size());
        log.info("NR Size:{}", NR.size());
        checkTickets(map, PT, TT, NR);
    }

    @Transactional
    private void checkTickets(Map<Integer, List<BookedTicketsAndStatus>> map, Map<Integer,
            List<PremiumTatkalTickets>> pt, Map<Integer, List<TatkalTickets>> tt, Map<Integer,
            List<NormalReservationTickets>> nr) {
        log.info("Request in checkTickets");
        for (Map.Entry<Integer, List<BookedTicketsAndStatus>> integerListEntry : map.entrySet()) {
            List<BookedTicketsAndStatus> list = integerListEntry.getValue();
            for (BookedTicketsAndStatus bookedTicketsAndStatus : list) {
                if (bookedTicketsAndStatus.getBookingMethod().equals("Premium Tatkal")) {
                    if (pt.containsKey(integerListEntry.getKey())) {
                        List<PremiumTatkalTickets> ticketsList = pt.get(integerListEntry.getKey());
                        List<PremiumTatkalTickets> finalPR = new ArrayList<>();
                        for (PremiumTatkalTickets premiumTatkalTickets : ticketsList) {
                            if (bookedTicketsAndStatus.getFromStationName().equals(premiumTatkalTickets.getStationName())
                                    || bookedTicketsAndStatus.getToStationName().equals(premiumTatkalTickets.getStationName())) {
                                finalPR.add(premiumTatkalTickets);
                            }
                        }
                        finalPRTicketchecks(bookedTicketsAndStatus, finalPR);
                    }
                } else if (bookedTicketsAndStatus.getBookingMethod().equals("Tatkal")) {
                    if (tt.containsKey(integerListEntry.getKey())) {
                        List<TatkalTickets> ticketsList = tt.get(integerListEntry.getKey());
                        List<TatkalTickets> finalTT = new ArrayList<>();
                        for (TatkalTickets tatkalTickets : ticketsList) {
                            if (bookedTicketsAndStatus.getFromStationName().equals(tatkalTickets.getStationName())
                                    || bookedTicketsAndStatus.getToStationName().equals(tatkalTickets.getStationName())) {
                                finalTT.add(tatkalTickets);
                            }
                        }
                        finalTTTicketchecks(bookedTicketsAndStatus, finalTT);
                    }
                } else {
                    if (nr.containsKey(integerListEntry.getKey())) {
                        List<NormalReservationTickets> ticketsList = nr.get(integerListEntry.getKey());
                        List<NormalReservationTickets> finalNR = new ArrayList<>();
                        for (NormalReservationTickets normalTickets : ticketsList) {
                            if (bookedTicketsAndStatus.getFromStationName().equals(normalTickets.getStationName())
                                    || bookedTicketsAndStatus.getToStationName().equals(normalTickets.getStationName())) {
                                finalNR.add(normalTickets);
                            }
                        }
                        finalNRTicketchecks(bookedTicketsAndStatus, finalNR);
                    }
                }
            }
        }
    }

    @Transactional
    private void finalNRTicketchecks(BookedTicketsAndStatus bookedTicketsAndStatus, List<NormalReservationTickets> finalNR) {
        log.info("Request in finalNRTicketchecks");
        for (NormalReservationTickets normalReservationTickets : finalNR) {
            List<PassengerDetails> passengerDetails = bookedTicketsAndStatus.getPassengersList();
            for (PassengerDetails passengerDetail : passengerDetails) {
                if (passengerDetail.getCoachName().equals(normalReservationTickets.getCoachName())) {
                    boolean bookingOccured = false;
                    int noOfTickets = bookedTicketsAndStatus.getNumberOfTickets();
                    if (bookedTicketsAndStatus.getFromStationName().equals(normalReservationTickets.getStationName())) {
                        int noOfTicketsBooked = normalReservationTickets.getNoOfSeatsBooked();
                        int noOfTicketsAvailabe = normalReservationTickets.getNoOfSeatsAvailable();
                        log.info("Station Name:{}", normalReservationTickets.getStationName());
                        if (bookedTicketsAndStatus.getNumberOfTickets() <= normalReservationTickets.getNoOfSeatsAvailable()) {
                            normalReservationTickets.setNoOfSeatsBooked(noOfTicketsBooked + noOfTickets);
                            normalReservationTickets.setNoOfSeatsAvailable(noOfTicketsAvailabe - noOfTickets);
                            bookedTicketsAndStatus.setBookingStatus(BookingStatus.CONFIRMED);
                            bookedTicketsAndStatus.setWaitingToConfirmTicket("YES");
                            bookingOccured = true;
                        }
                    } else if (bookedTicketsAndStatus.getToStationName().equals(normalReservationTickets.getStationName())) {
                        if (bookingOccured) {
                            int noOfTicketsBooked = normalReservationTickets.getNoOfSeatsBooked();
                            int noOfTicketsAvailabe = normalReservationTickets.getNoOfSeatsAvailable();
                            log.info("Station Name:{}", normalReservationTickets.getStationName());
                            normalReservationTickets.setNoOfSeatsAvailable(noOfTicketsAvailabe + noOfTickets);
                        }
                    }
                }
            }

        }

    }

    @Transactional
    private void finalTTTicketchecks(BookedTicketsAndStatus bookedTicketsAndStatus, List<TatkalTickets> finalPR) {
        log.info("Request in finalNRTicketchecks");
        for (TatkalTickets tatkalTickets : finalPR) {
            List<PassengerDetails> passengerDetails = bookedTicketsAndStatus.getPassengersList();
            for (PassengerDetails passengerDetail : passengerDetails) {
                if (passengerDetail.getCoachName().equals(tatkalTickets.getCoachName())) {
                    boolean bookingOccured = false;
                    int noOfTickets = bookedTicketsAndStatus.getNumberOfTickets();
                    if (bookedTicketsAndStatus.getFromStationName().equals(tatkalTickets.getStationName())) {
                        int noOfTicketsBooked = tatkalTickets.getNoOfSeatsBooked();
                        int noOfTicketsAvailabe = tatkalTickets.getNoOfSeatsAvailable();
                        log.info("Station Name:{}", tatkalTickets.getStationName());
                        if (bookedTicketsAndStatus.getNumberOfTickets() <= tatkalTickets.getNoOfSeatsAvailable()) {
                            tatkalTickets.setNoOfSeatsBooked(noOfTicketsBooked + noOfTickets);
                            tatkalTickets.setNoOfSeatsAvailable(noOfTicketsAvailabe - noOfTickets);
                            bookedTicketsAndStatus.setBookingStatus(BookingStatus.CONFIRMED);
                            bookedTicketsAndStatus.setWaitingToConfirmTicket("YES");
                            bookingOccured = true;
                        }
                    } else if (bookedTicketsAndStatus.getToStationName().equals(tatkalTickets.getStationName())) {
                        if (bookingOccured) {
                            int noOfTicketsBooked = tatkalTickets.getNoOfSeatsBooked();
                            int noOfTicketsAvailabe = tatkalTickets.getNoOfSeatsAvailable();
                            log.info("Station Name:{}", tatkalTickets.getStationName());
                            tatkalTickets.setNoOfSeatsAvailable(noOfTicketsAvailabe + noOfTickets);
                        }
                    }
                }
            }
        }
    }

    @Transactional
    private void finalPRTicketchecks(BookedTicketsAndStatus bookedTicketsAndStatus, List<PremiumTatkalTickets> finalPR) {
        log.info("Request in finalNRTicketchecks");
        for (PremiumTatkalTickets premiumTatkalTickets : finalPR) {
            List<PassengerDetails> passengerDetails = bookedTicketsAndStatus.getPassengersList();
            for (PassengerDetails passengerDetail : passengerDetails) {
                if (passengerDetail.getCoachName().equals(premiumTatkalTickets.getCoachName())) {
                    boolean bookingOccured = false;
                    int noOfTickets = bookedTicketsAndStatus.getNumberOfTickets();
                    if (bookedTicketsAndStatus.getFromStationName().equals(premiumTatkalTickets.getStationName())) {
                        int noOfTicketsBooked = premiumTatkalTickets.getNoOfSeatsBooked();
                        int noOfTicketsAvailabe = premiumTatkalTickets.getNoOfSeatsAvailable();
                        log.info("Station Name:{}", premiumTatkalTickets.getStationName());
                        if (bookedTicketsAndStatus.getNumberOfTickets() <= premiumTatkalTickets.getNoOfSeatsAvailable()) {
                            premiumTatkalTickets.setNoOfSeatsBooked(noOfTicketsBooked + noOfTickets);
                            premiumTatkalTickets.setNoOfSeatsAvailable(noOfTicketsAvailabe - noOfTickets);
                            bookedTicketsAndStatus.setBookingStatus(BookingStatus.CONFIRMED);
                            bookedTicketsAndStatus.setWaitingToConfirmTicket("YES");
                            bookingOccured = true;
                        }
                    } else if (bookedTicketsAndStatus.getToStationName().equals(premiumTatkalTickets.getStationName())) {
                        if (bookingOccured) {
                            int noOfTicketsBooked = premiumTatkalTickets.getNoOfSeatsBooked();
                            int noOfTicketsAvailabe = premiumTatkalTickets.getNoOfSeatsAvailable();
                            log.info("Station Name:{}", premiumTatkalTickets.getStationName());
                            premiumTatkalTickets.setNoOfSeatsAvailable(noOfTicketsAvailabe + noOfTickets);
                        }
                    }
                }
            }
        }
    }

    @Transactional
    public ResponseEntity<String> bookingCancelRequest(BookingCancelRequestDTO bookingCancelRequestDTO) {
        log.info("Request In BookingCancelRequest:{}", bookingCancelRequestDTO);
        BookedTicketsAndStatus bookedTicketsAndStatus = bookedTicketsRepo.findByPnr(bookingCancelRequestDTO.getPnr());
        PassengerDetails details = passengerDetailsRepo.findByPnr(bookingCancelRequestDTO.getPnr());
        log.info("BookedTicketsAndStatus:{}", bookedTicketsAndStatus);
        if (bookedTicketsAndStatus.getBookingStatus().equals(BookingStatus.CONFIRMED)) {
            if (!bookedTicketsAndStatus.getIsCancellingTicketsClosed()) {
                double eachTicketPrice = bookedTicketsAndStatus.getAmount();
                bookingServiceToPaymentService.paymentReturn(bookedTicketsAndStatus.getTransactionID(), eachTicketPrice);
                bookedTicketsAndStatus.setBookingStatus(BookingStatus.CANCELLED);
                callCancellationBookingEvent(bookedTicketsAndStatus, details);
                CheckAnyWaitingListTicket(bookedTicketsAndStatus);
            } else {
                log.info("Cancelling Request Not Available");
            }
        } else {
            log.info("WAITING Tickets Cant be Cancelled!");
            return ResponseEntity.badRequest().body("WAITING Tickets Cant be Cancelled!");
        }


        return ResponseEntity.ok().body("OK");
    }

    private void callCancellationBookingEvent(BookedTicketsAndStatus bookedTicketsAndStatus, PassengerDetails details) {
        PassengerDetailsResponse detailsResponse = new PassengerDetailsResponse(
                details.getPnr(), details.getPassengerName(), details.getGender(), details.getAge(),
                details.getCoachName(), details.getCoachNumber(), details.getSeatNumber());
        BookingResponse response = new BookingResponse(
                bookedTicketsAndStatus.getPnr(), bookedTicketsAndStatus.getUserName(),
                bookedTicketsAndStatus.getTrainNumber(), bookedTicketsAndStatus.getTravelDate(),
                bookedTicketsAndStatus.getFromStationName(), bookedTicketsAndStatus.getToStationName(), 1,
                bookedTicketsAndStatus.getBookingMethod(), bookedTicketsAndStatus.getAmount(), bookedTicketsAndStatus.getWaitingToConfirmTicket(), bookedTicketsAndStatus.getTransactionID(), BookingStatus.CANCELLED, List.of(detailsResponse));
        bookingEvent.sendBookingResponseToUser(response);

    }

    @Transactional
    private void CheckAnyWaitingListTicket(BookedTicketsAndStatus bookedTicketsAndStatus) {
        PassengerDetails passengerDetails = passengerDetailsRepo.findByPnr(bookedTicketsAndStatus.getPnr());
        List<BookedTicketsAndStatus> bookTicketsList = bookedTicketsRepo.findAllByBookingStatusAndTravelDate(BookingStatus.WAITING, bookedTicketsAndStatus.getTravelDate());
        for (BookedTicketsAndStatus ticketsAndStatus : bookTicketsList) {
            if (ticketsAndStatus.getTrainNumber().equals(bookedTicketsAndStatus.getTrainNumber())) {
                List<PassengerDetails> waitingPassengerList = ticketsAndStatus.getPassengersList();
                for (PassengerDetails details : waitingPassengerList) {
                    if ((details.getCoachName().equals(passengerDetails.getCoachName()))) {
                        if (ticketsAndStatus.getFromStationName().equals(bookedTicketsAndStatus.getFromStationName())
                                && ticketsAndStatus.getToStationName().equals(bookedTicketsAndStatus.getToStationName())) {
                            ticketsAndStatus.setBookingStatus(BookingStatus.CONFIRMED);
                            ticketsAndStatus.setWaitingToConfirmTicket("YES");
                            PassengerDetails newPassengerDetails = passengerDetailsRepo.findByPnr(ticketsAndStatus.getPnr());
                            newPassengerDetails.setCoachName(details.getCoachName());
                            newPassengerDetails.setCoachNumber(details.getCoachNumber());
                            newPassengerDetails.setSeatNumber(details.getSeatNumber());
                            details.setSeatNumber(0);
                            details.setCoachName("Null");
                            details.setCoachNumber("Null");
                        }
                    }
                }
            }
        }
    }

    public BookedTicketsAndStatus getBookedTicketsAndStatusByPNR(String pnr) {
        return bookedTicketsRepo.findByPnr(pnr);
    }


    public void getLastTrainNumberAndTravelDay() {

    }

    public void getNextDayNormalReservationTickets() {


    }


    public Map<Integer, LocalDate> getTrainNumberAndTravelDay() {
        return trainNumberAndLastTravelDate;
    }

    public void clearTatkalAndPremiumTatkalRecord() {
        tatkalRepo.deleteAll();
        premiumTatkalRepo.deleteAll();
    }

//    @Transactional
//    public void clearNormalTickets() {
//        normalReservationRepo.deleteAllByTravelDate(LocalDate.now());
//    }


    public void getDistinctNormalReservationTickets() {
        List<NormalReservationTickets> list = normalReservationRepo.findTopRowPerTrainNumber();
        log.info("list:{}", list);
        for (NormalReservationTickets tickets : list) {
            TrainNumberTravelDateStartingTime ts = new TrainNumberTravelDateStartingTime(tickets.getTrainNumber(),
                    tickets.getTravelDate(), tickets.getStartingTime());
            trainNumberTravelDateStartingTimes.put(tickets.getTrainNumber(), ts);
        }
        log.info("Map:{}", trainNumberTravelDateStartingTimes);
    }

    @Transactional
    public void closingExistingTickets(Integer trainNumber, LocalDate travelDate) {
        List<BookedTicketsAndStatus> bookedTickets = bookedTicketsRepo.findAllByTrainNumberAndTravelDate(trainNumber, travelDate);
        log.info("bookedTickets:{}", bookedTickets);
        for (BookedTicketsAndStatus bookedTicket : bookedTickets) {
            bookedTicket.setIsCancellingTicketsClosed(true);
        }
    }

    @Transactional
    public void clearNormalTickets(Integer trainNumber, LocalDate travelDate) {
        normalReservationRepo.deleteAllByTrainNumberAndTravelDate(trainNumber, travelDate);
    }
}
