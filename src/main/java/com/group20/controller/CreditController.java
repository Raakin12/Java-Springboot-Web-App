package com.group20.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.group20.service.CreditService;

@RestController
@RequestMapping("/api/credit")
public class CreditController {

	private CreditService creditService;

	public CreditController(CreditService creditService) {

		this.creditService = creditService;
	}

	@GetMapping("/check/{creditId}")
	public ResponseEntity<Double> checkCredit(@PathVariable Long creditId) {
		double creditAmount = creditService.getCreditAmount(creditId);
		if (creditAmount == -1.0) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(creditAmount);
		}
	}

	@PostMapping("/confirm/{creditId}")
	public ResponseEntity<Void> confirmCredit(@PathVariable Long creditId) {
		System.out.println("Confirming credit ID: " + creditId);
		boolean isRemoved = creditService.removeCredit(creditId);
		if (isRemoved) {
			System.out.println("Credit removed successfully for ID: " + creditId);
			return ResponseEntity.ok().build();
		} else {
			System.out.println("Credit ID not found: " + creditId);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/cancellation/{bookingId}")
	public ResponseEntity<Double> GetCredit(
			@PathVariable Long bookingId,
			@RequestParam(value = "guestEmail", required = false) String guestEmail) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()
				&& authentication.getAuthorities().stream().noneMatch(
						authority -> authority.getAuthority().equals("ROLE_ANONYMOUS"))) {

			String email = authentication.getName();
			double creditAmount = creditService.gainCreditUser(bookingId, email);

			if (creditAmount == -1) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"You cannot cancel a booking within 72 hours of the showtime.");
			} else if (creditAmount == -2) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking ID does not exist.");
			} else if (creditAmount == -3) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"The provided email does not match the booking ID.");
			}

			return ResponseEntity.ok(creditAmount);

		} else {
			if (guestEmail == null || guestEmail.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Guest email is required for cancellation.");
			}

			double creditAmount = creditService.gainCreditGuest(bookingId, guestEmail);

			if (creditAmount == -1) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"You cannot cancel a booking within 72 hours of the showtime.");
			} else if (creditAmount == -2) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking ID does not exist.");
			} else if (creditAmount == -3) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"The provided email does not match the booking ID.");
			}

			return ResponseEntity.ok(creditAmount);
		}
	}

}
