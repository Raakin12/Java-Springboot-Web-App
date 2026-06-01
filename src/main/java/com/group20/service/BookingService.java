package com.group20.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.group20.Repository.BookingRepository;
import com.group20.Repository.ShowtimeRepository;
import com.group20.Strategy.BookingObserver;
import com.group20.model.Booking;
import com.group20.model.Seat;
import com.group20.model.Showtime;

@Service
public class BookingService {

	private final BookingRepository bookingRepository;
	private final ShowtimeRepository showtimeRepository;

	private final List<BookingObserver> bookingObservers;
	private final NotificationService notificationService;

	public BookingService(BookingRepository bookingRepository, ShowtimeRepository showtimeRepository,
			List<BookingObserver> bookingObservers,
			NotificationService notificationService) {
		this.bookingRepository = bookingRepository;
		this.showtimeRepository = showtimeRepository;
		this.bookingObservers = bookingObservers;
		this.notificationService = notificationService;
	}

	public void createBooking(String email, String theatreName, String movieName, String showtimeName,
			String seatNumber, Showtime showtime, Seat seat, String date) {

		Booking booking = new Booking(null, email, Date.valueOf(LocalDate.now()), seat, showtime);
		bookingRepository.save(booking);
		notifyBookingObservers(booking);

		notificationService.sendBookingConfirmationEmail(email, booking.getId(), movieName, showtimeName, theatreName,
				seatNumber, date);
		notificationService.sendReceiptConfirmationEmail(email, booking.getId(), movieName, seat.getPrice());

	}

	public Booking validateAndFetchBooking(Long bookingId, String email) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new IllegalArgumentException("Booking ID does not exist"));

		if (!booking.getEmail().equals(email)) {
			throw new IllegalArgumentException("Email does not match the booking's email");
		}

		return booking;
	}

	public boolean isBookingCancellable(Showtime showtime) {
		LocalDate showLocalDate = showtime.getShowDate().toLocalDate();
		LocalTime showLocalTime = showtime.getShowTime().toLocalTime();
		LocalDateTime showDateTime = LocalDateTime.of(showLocalDate, showLocalTime);

		LocalDateTime now = LocalDateTime.now();

		long hoursUntilShow = ChronoUnit.HOURS.between(now, showDateTime);
		return hoursUntilShow >= 72;
	}

	public void cancelBooking(Long bookingId) {
		bookingRepository.deleteById(bookingId);
	}

	public void incrementShowtimeCapacity(Showtime showtime) {
		showtimeRepository.incrementCapacity(showtime.getId());
	}

	private void notifyBookingObservers(Booking booking) {
		for (BookingObserver observer : bookingObservers) {
			observer.update(booking);
		}
	}
}
