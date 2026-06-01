package com.group20.model;

import java.sql.Date;
import java.sql.Time;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;

@Entity
public class Showtime {
	private int maxCapacity = 80;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "show_id", nullable = false)
	private Long id;

	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "movie_id")
	private Movie movie;

	@Column(name = "show_time", nullable = false)
	private Time showTime;

	@Column(nullable = false)
	private int capacity = 80;

	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "theatre_id")
	private Theatre theatre;

	@Column(name = "show_date", nullable = false)
	private Date showDate;

	@Transient
	private boolean booked = false;

	public Showtime() {
	}

	public Showtime(Long id, Movie movie, Time showTime, Theatre theatre) {

		this.id = id;
		this.movie = movie;
		this.showTime = showTime;
		this.theatre = theatre;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Time getShowTime() {
		return showTime;
	}

	public void setShowTime(Time showTime) {
		this.showTime = showTime;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public void booking() {
		this.capacity--;
	}

	public void cancel() {
		this.capacity++;
	}

	public Theatre getTheatre() {
		return theatre;
	}

	public void setTheatre(Theatre theatre) {
		this.theatre = theatre;
	}

	public Date getShowDate() {
		return showDate;
	}

	public void setShowDate(Date showDate) {
		this.showDate = showDate;
	}

	public boolean isBooked() {
		return booked;
	}

	public void setBooked(boolean booked) {
		this.booked = booked;
	}

}
