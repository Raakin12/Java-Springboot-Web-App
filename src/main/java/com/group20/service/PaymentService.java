package com.group20.service;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Service;

import com.group20.Repository.MovieRepository;
import com.group20.Repository.SeatRepository;
import com.group20.Repository.ShowtimeRepository;
import com.group20.Repository.TheatreRepository;
import com.group20.Repository.UserRepository;
import com.group20.Strategy.Payment;
import com.group20.Strategy.PaymentFactory;
import com.group20.model.Seat;
import com.group20.model.Showtime;

@Service
public class PaymentService {

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final BookingService bookingService;

    public PaymentService(ShowtimeRepository showtimeRepository, 
                          MovieRepository movieRepository,
                          TheatreRepository theatreRepository, 
                          SeatRepository seatRepository,
                          BookingService bookingService, 
                          UserRepository userRepository) {
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
        this.theatreRepository = theatreRepository;
        this.seatRepository = seatRepository;
        this.bookingService = bookingService;
        this.userRepository = userRepository;
    }

   
    public void paymentConfirmation(String email, Long seatId, Long showtimeId, Long movieId, Long theatreId, 
                                     Payment paymentMethod, boolean useSavedMethod) {
        processPayment(email, paymentMethod, useSavedMethod);

        String theatreName = theatreRepository.findNameById(theatreId);
        String movieName = movieRepository.findNameById(movieId);
        String showtimeName = formatTime(showtimeRepository.findShowTimeByShowId(showtimeId));
        String seatNumber = seatRepository.findSeatNumberBySeatId(seatId);
        
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Showtime not found with ID: " + showtimeId));
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found with ID: " + seatId));
        String date = formatDate(showtime.getShowDate());

        bookingService.createBooking(email, theatreName, movieName, showtimeName, seatNumber, showtime, seat, date);
    }

  
    private void processPayment(String email, Payment paymentMethod, boolean useSavedMethod) {
        if (useSavedMethod) {
            String savedPaymentType = userRepository.findPaymentByEmail(email);
            Payment savedPaymentMethod = PaymentFactory.getPaymentStrategy(savedPaymentType);
            savedPaymentMethod.pay();
        } else {
            paymentMethod.pay();
        }
    }

   
    private String formatDate(java.util.Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

   
    private String formatTime(java.util.Date time) {
        return new SimpleDateFormat("hh:mm a").format(time);
    }
}
