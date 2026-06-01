package com.group20.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.group20.Repository.SeatRepository;
import com.group20.Strategy.BookingObserver;
import com.group20.Strategy.CancellationObserver;
import com.group20.Strategy.ShowtimeObserver;
import com.group20.model.Booking;
import com.group20.model.Seat;

import jakarta.transaction.Transactional;

@Service
public class SeatService implements ShowtimeObserver, BookingObserver, CancellationObserver {

	private final SeatRepository seatRepository;

	public SeatService(SeatRepository seatRepository) {
		this.seatRepository = seatRepository;
	}

 
	public List<Seat> retrieveSeats(Long showtimeId) {
		return seatRepository.findSeatsByShowtimeId(showtimeId);
	}

	 
	@Override
	public void update(Booking booking) {
		Seat seat = booking.getSeat();
		if (seat == null) {
			throw new IllegalArgumentException("Seat not found in booking.");
		}
		seat.setBooked(true);
		seatRepository.save(seat);
	}

	 
	@Override
	public void update(Long showtimeId) {
		List<Seat> showtimeSeats = seatRepository.findSeatsByShowtimeId(showtimeId);
		if (showtimeSeats.isEmpty()) {
			System.out.println("No seats found for showtime ID: " + showtimeId);
			return;
		}
		for (Seat seat : showtimeSeats) {
			seat.setBooked(false);
			seatRepository.save(seat);
		}
	}

	 

	@Transactional
	public void cancel(Booking booking) {
		if (booking == null || booking.getSeat() == null) {
			throw new IllegalArgumentException("Invalid booking or seat.");
		}

		Seat seat = booking.getSeat();
		if (seat == null) {
			throw new IllegalArgumentException("Seat not found for booking.");
		}
		System.out.println("attempting to clear seat : " + booking.getSeat());

		seat.setBooked(false);
		seatRepository.save(seat);
	}

	@Transactional
	public void cancelSeat(Booking booking) {
		cancel(booking);
	}
}
