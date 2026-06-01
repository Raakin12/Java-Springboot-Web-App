package com.group20.model;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Seat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seat_id", nullable = false)
	private Long id;

	@Column(name = "seat_number", nullable = false)
	private String seatNumber;

	@Column(nullable = false)
	private boolean booked = false;

	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "show_id", nullable = false)
	private Showtime showtime;

	public Seat() {
	}

	private double price;

	public Seat(Long id, String seatNumber, boolean booked, Showtime showtime) {

		this.id = id;
		this.seatNumber = seatNumber;
		this.booked = booked;
		this.showtime = showtime;
		this.price = calculatePrice(seatNumber);

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public boolean isBooked() {
		return booked;
	}

	public void setBooked(boolean booked) {
		this.booked = booked;
	}

	public Showtime getShowtime() {
		return showtime;
	}

	public void setShowtime(Showtime showtime) {
		this.showtime = showtime;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(String seatNumber) {
		this.price = calculatePrice(seatNumber);
	}

	private double calculatePrice(String seatNumber) {
		char row = seatNumber.charAt(0); 
		if (row >= 'G' && row <= 'H') {
			return 40.0;
		} else {
			return 20.0;
		}
	}

}
