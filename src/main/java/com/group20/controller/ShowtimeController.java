package com.group20.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group20.model.Showtime;
import com.group20.service.ShowtimeService;

@RestController
@RequestMapping("/api/{theatreId}/{movieId}/showtime")
public class ShowtimeController {

	private final ShowtimeService showtimeService;

	public ShowtimeController(ShowtimeService showtimeService) {

		this.showtimeService = showtimeService;
	}

	@GetMapping
	public ResponseEntity<List<Showtime>> getShowtime(@PathVariable("theatreId") Long theatreId,
			@PathVariable("movieId") Long movieId) {
		return ResponseEntity.ok(showtimeService.movieShowtime(theatreId, movieId));
	}

	@GetMapping("/prebook")
	public ResponseEntity<List<Showtime>> getShowtimePrebook(@PathVariable("theatreId") Long theatreId,
			@PathVariable("movieId") Long movieId) {
		return ResponseEntity.ok(showtimeService.preReleasedShowtime(movieId, theatreId));
	}

}
