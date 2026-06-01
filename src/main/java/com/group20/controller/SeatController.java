package com.group20.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group20.model.Seat;
import com.group20.service.SeatService;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/{showtimeId}/seats")
public class SeatController {

	private final SeatService seatService;

	public SeatController(SeatService seatService) {

		this.seatService = seatService;
	}

	@GetMapping
	public ResponseEntity<List<Seat>> getSeatMap(@PathVariable("showtimeId") Long showtimeId) {
		return ResponseEntity.ok(seatService.retrieveSeats(showtimeId));
	}

	@GetMapping("/{seatId}")
	public ResponseEntity<Seat> getSeatById(@PathVariable("showtimeId") Long showtimeId,
			@PathVariable("seatId") Long seatId) {
		return seatService.retrieveSeats(showtimeId).stream()
				.filter(seat -> seat.getId().equals(seatId))
				.findFirst()
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
}
