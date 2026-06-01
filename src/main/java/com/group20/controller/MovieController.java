package com.group20.controller;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.group20.model.Movie;
import com.group20.service.MovieService;

@RestController
@RequestMapping("/api/Movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {

        this.movieService = movieService;
    }

    @GetMapping("/available")
    public ResponseEntity<List<Movie>> getMoviesAvailable() {
        return ResponseEntity.ok(movieService.getMoviesByStatus(Movie.MovieStatus.AVAILABLE));
    }

    @GetMapping("/restricted")
    public ResponseEntity<List<Movie>> getMoviesRestricted(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(403).build(); 
        }
        return ResponseEntity.ok(movieService.getMoviesByStatus(Movie.MovieStatus.RESTRICTED));
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<?> getMovieById(@PathVariable("movieId") Long movieId, Authentication authentication) {
        Movie movie = movieService.getMovieById(movieId);

        if (movie == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found");
        }

        switch (movie.getStatus()) {
            case AVAILABLE:
                return ResponseEntity.ok(movie); 

            case RESTRICTED:
                if (authentication == null || !authentication.isAuthenticated()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Restricted content. Please log in.");
                }
                return ResponseEntity.ok(movie); 

            case UNAVAILABLE:
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This movie is not yet available."); 

            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected status.");
        }
    }
}
