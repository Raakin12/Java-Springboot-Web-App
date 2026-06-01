package com.group20.service;

import com.group20.Repository.MovieRepository;
import com.group20.model.Movie;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MovieStatusUpdateService {

    private final MovieRepository movieRepository;

    public MovieStatusUpdateService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    
    @Scheduled(cron = "0 * * * * ?") 
    public void updateMovieStatuses() {
        LocalDate today = LocalDate.now();
        LocalDate nextMonth = today.plusMonths(1);

        movieRepository.findAll().forEach(movie -> {
            LocalDate releaseDate = movie.getReleasedDate().toLocalDate();
            if (releaseDate.isBefore(today)) {
                movie.setStatus(Movie.MovieStatus.AVAILABLE);
            } else if (releaseDate.isBefore(nextMonth)) {
                movie.setStatus(Movie.MovieStatus.RESTRICTED);
            } else {
                movie.setStatus(Movie.MovieStatus.UNAVAILABLE);
            }
            movieRepository.save(movie);
        });
    }
}
