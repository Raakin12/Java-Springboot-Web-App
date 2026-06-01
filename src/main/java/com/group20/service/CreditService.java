package com.group20.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.group20.Repository.BookingRepository;
import com.group20.Repository.CreditRepository;
import com.group20.Strategy.CancellationObserver;
import com.group20.model.Booking;
import com.group20.model.Credit;
import com.group20.model.Seat;

import jakarta.transaction.Transactional;

@Service
public class CreditService {

	private final BookingRepository bookingRepository;
	private final CreditRepository creditRepository;
	private final List<CancellationObserver> cancellationObservers;
	private final SeatService seatService;
	private final BookingService bookingService;
	private final NotificationService notificationService;

	public CreditService(BookingRepository bookingRepository,
			CreditRepository creditRepository,
			List<CancellationObserver> cancellationObservers,
			SeatService seatService,
			BookingService bookingService,
			NotificationService notificationService) {

		this.bookingRepository = bookingRepository;
		this.creditRepository = creditRepository;
		this.cancellationObservers = cancellationObservers;
		this.seatService = seatService;
		this.bookingService = bookingService;
		this.notificationService = notificationService;
	}

	public double getCreditAmount(Long creditId) {
		return creditRepository.findById(creditId)
				.map(Credit::getAmount)
				.orElse(-1.0);
	}

	public boolean removeCredit(Long creditId) {
		if (creditRepository.existsById(creditId)) {
			creditRepository.deleteById(creditId);
			return true;
		}
		return false;
	}

	public double gainCreditUser(Long bookingId, String email) {
		Booking booking = bookingService.validateAndFetchBooking(bookingId, email);
		if (!bookingService.isBookingCancellable(booking.getShowtime())) {
			return -1; 
		}

		bookingService.cancelBooking(bookingId);
		seatService.cancelSeat(booking);

		Credit credit = createCredit(booking.getSeat().getPrice());
		creditRepository.save(credit);

		notifyCancellationObservers(booking);

		notificationService.sendCancellationEmail(email, credit.getId(), credit.getExpiry().toString(),
				credit.getAmount());

		return credit.getAmount();
	}

	@Transactional
	public double gainCreditGuest(Long bookingId, String email) {
		Booking booking = bookingService.validateAndFetchBooking(bookingId, email);
		if (!bookingService.isBookingCancellable(booking.getShowtime())) {
			return -1; 
		}

		bookingService.cancelBooking(bookingId);
		seatService.cancelSeat(booking);

		double discountedAmount = booking.getSeat().getPrice() * 0.85;
		Credit credit = createCredit(discountedAmount);
		creditRepository.save(credit);

		bookingService.incrementShowtimeCapacity(booking.getShowtime());

		notificationService.sendCancellationEmail(email, credit.getId(), credit.getExpiry().toString(),
				credit.getAmount());

		return credit.getAmount();
	}


	private Credit createCredit(double amount) {
		LocalDate expiryDate = LocalDate.now().plusYears(1);
		return new Credit(null, Date.valueOf(expiryDate), amount);
	}

	private void notifyCancellationObservers(Booking booking) {
		for (CancellationObserver observer : cancellationObservers) {
			observer.cancel(booking);
		}
	}
}
