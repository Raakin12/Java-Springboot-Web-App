package com.group20.model;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;

import java.util.Random;
import java.util.UUID;
import java.sql.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Booking {

	@Id
	@Column(name = "booking_id", nullable = false, unique = true, length = 16)
	private Long id;

	@Column(nullable = false)
	private String email;

	@Column(name = "booking_date", nullable = false)
	private Date bookingDate;

	@OneToOne
	@JoinColumn(name = "seat_id", referencedColumnName = "seat_id", nullable = false)
	private Seat seat;

	@ManyToOne
	@JoinColumn(name = "showtime_id", referencedColumnName = "show_id", nullable = false)
	private Showtime showtime;

	public Booking() {
	}

	public Booking(Long id, String email, Date bookingDate, Seat seat, Showtime showtime) {
		super();
		this.id = id;
		this.email = email;
		this.bookingDate = bookingDate;
		this.seat = seat;
		this.showtime = showtime;
	}

	@PrePersist
	private void generateId() {
		if (this.id == null) {
			this.id = generateRandomLongId();
		}
	}

	private Long generateRandomLongId() {
		Random random = new Random();
		long lowerBound = 1_000_000_000_000_000L;
		long upperBound = 9_999_999_999_999_999L; 
		return lowerBound + (long) (random.nextDouble() * (upperBound - lowerBound));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}

	public Showtime getShowtime() {
		return showtime;
	}

	public void setShowtime(Showtime showtime) {
		this.showtime = showtime;
	}

}
