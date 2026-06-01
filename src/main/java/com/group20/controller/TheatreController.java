package com.group20.controller;

import com.group20.model.Theatre;
import com.group20.service.TheatreService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/theatres")
public class TheatreController {

	private final TheatreService theatreService;

	public TheatreController(TheatreService theatreService) {

		this.theatreService = theatreService;
	}

	@GetMapping
	public ResponseEntity<List<Theatre>> getTheatres() {
		return ResponseEntity.ok(theatreService.getAllTheatres());
	}

	@GetMapping("/{theatreId}")
	public ResponseEntity<Theatre> getTheatreById(@PathVariable Long theatreId) {
		Theatre theatre = theatreService.getTheatreById(theatreId);
		if (theatre != null) {
			return ResponseEntity.ok(theatre);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

}
