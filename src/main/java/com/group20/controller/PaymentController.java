package com.group20.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.group20.Strategy.Payment;
import com.group20.Strategy.PaymentFactory;
import com.group20.service.PaymentService;

@RestController
@RequestMapping("/api/{theatreId}/{movieId}/{showtimeId}/{seatId}/payment")
public class PaymentController {
	
	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {

		this.paymentService = paymentService;
	}
	
	@PostMapping
	public void processPayment(@PathVariable("theatreId") Long theatreId,
    @PathVariable("movieId") Long movieId,
    @PathVariable("showtimeId") Long showtimeId,
    @PathVariable("seatId") Long seatId,
    @RequestParam("method") String paymentMethod,
    @RequestParam(value = "guestEmail", required = false) String guestEmail){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email;
		
		if (authentication != null && authentication.isAuthenticated()
                && authentication.getAuthorities().stream().noneMatch(
                    authority -> authority.getAuthority().equals("ROLE_ANONYMOUS"))){
			email = authentication.getName();
		
			if ("SavedPayment".equals(paymentMethod)){
				paymentService.paymentConfirmation(email, seatId, showtimeId, movieId, theatreId,null,true);
			}
			else {
				Payment method = PaymentFactory.getPaymentStrategy(paymentMethod);
				paymentService.paymentConfirmation(email, seatId, showtimeId, movieId, theatreId,method,false);
			}
		}
		else if (guestEmail != null) {
	        Payment method = PaymentFactory.getPaymentStrategy(paymentMethod);
	        paymentService.paymentConfirmation(guestEmail, seatId, showtimeId, movieId, theatreId, method, false);
	    } 
		else {
	        throw new IllegalArgumentException("Guest email is required for unauthenticated users");
	    }
	}
}
