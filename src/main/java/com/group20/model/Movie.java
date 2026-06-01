package com.group20.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;

@Entity
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String genre;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Column(name = "released_date", nullable = false)
	private Date releasedDate;

	@Column(nullable = false, length = 1000)
	private String description;

	@Column(nullable = false)
	private double rating;

	@Column(nullable = false)
	private int duration;

	@Column(nullable = false)
	private String imageUrl;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MovieStatus status;

	public enum MovieStatus {
		AVAILABLE,
		RESTRICTED,
		UNAVAILABLE
	}

	public Movie() {
	}

	public Movie(String title, String genre, Date releasedDate, String description,
			double rating, int duration, String imageUrl, MovieStatus status) {
		this.title = title;
		this.genre = genre;
		this.releasedDate = releasedDate;
		this.description = description;
		this.rating = rating;
		this.duration = duration;
		this.imageUrl = imageUrl;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public Date getReleasedDate() {
		return releasedDate;
	}

	public void setReleasedDate(Date releasedDate) {
		this.releasedDate = releasedDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public MovieStatus getStatus() {
		return status;
	}

	public void setStatus(MovieStatus status) {
		this.status = status;
	}
}
