package com.group20.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.group20.Repository.MovieRepository;
import com.group20.model.Movie;
import com.group20.model.Movie.MovieStatus;

@Service
public class MovieService {

	private final MovieRepository movieRepository;

	public MovieService(MovieRepository movieRepository) {

		this.movieRepository = movieRepository;
	}

	public Movie getMovieById(Long movieId) {
		return movieRepository.findById(movieId).orElse(null);
	}

	public List<Movie> getMoviesByStatus(Movie.MovieStatus status) {
		return movieRepository.findByStatus(status);
	}

}