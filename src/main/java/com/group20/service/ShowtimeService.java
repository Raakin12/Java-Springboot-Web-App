package com.group20.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.group20.Repository.ShowtimeRepository;
import com.group20.Strategy.BookingObserver;
import com.group20.Strategy.CancellationObserver;
import com.group20.Strategy.ShowtimeObserver;
import com.group20.model.Booking;
import com.group20.model.Showtime;

import jakarta.transaction.Transactional;

@Service
public class ShowtimeService implements ShowtimeObserver, BookingObserver, CancellationObserver {

	private final ShowtimeRepository showtimeRepository;

	public ShowtimeService(ShowtimeRepository showtimeRepository) {
		this.showtimeRepository = showtimeRepository;
	}

	public List<Showtime> movieShowtime(Long theatreId, Long movieId) {
		return showtimeRepository.findByTheatreIdAndMovieId(theatreId, movieId);
	}

	public List<Showtime> preReleasedShowtime(Long movieId, Long theatreId) {
		List<Showtime> showtimes = showtimeRepository.findByTheatreIdAndMovieId(theatreId, movieId);

		for (Showtime showtime : showtimes) {
			boolean isWithinThreshold = showtime.getCapacity() <= showtime.getMaxCapacity() * 0.9; // removed 72 hardcoded
			showtime.setBooked(isWithinThreshold);
		}

		return showtimeRepository.saveAll(showtimes);
	}

	@Override
	public void update(Booking booking) {
		Showtime showtime = booking.getShowtime();
		showtime.booking();
		boolean isFull = showtime.getCapacity() <= 0;
		showtime.setBooked(isFull);
		showtimeRepository.save(showtime);
	}

	@Override
	public void update(Long showtimeId) {
		Showtime showtime = showtimeRepository.findById(showtimeId)
				.orElseThrow(() -> new RuntimeException("Showtime not found"));

		LocalDate nextWeekDate = showtime.getShowDate().toLocalDate().plusWeeks(1);
		showtime.setShowDate(Date.valueOf(nextWeekDate));
		showtime.setCapacity(showtime.getMaxCapacity()); // removed 80 hardcoded

		showtimeRepository.save(showtime);
	}

	@Override
	public void cancel(Booking booking) {
		Showtime showtime = booking.getShowtime();
		showtime.cancel();
		showtime.setBooked(false);
		showtimeRepository.save(showtime);
	}

	@Transactional
	public void incrementCapacity(Long showtimeId) {
		int rowsUpdated = showtimeRepository.incrementCapacity(showtimeId);

		if (rowsUpdated == 0) {
			throw new IllegalArgumentException("Showtime not found or cannot increment capacity for ID: " + showtimeId);
		}
	}
}
