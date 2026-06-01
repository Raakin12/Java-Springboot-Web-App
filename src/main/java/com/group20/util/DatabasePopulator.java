package com.group20.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.group20.Repository.MovieRepository;
import com.group20.Repository.ShowtimeRepository;
import com.group20.Repository.TheatreRepository;
import com.group20.Repository.SeatRepository;
import com.group20.model.Movie;
import com.group20.model.Showtime;
import com.group20.model.Theatre;
import com.group20.model.Seat;
import com.group20.Config.SeatLayoutConfig;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Component
public class DatabasePopulator implements CommandLineRunner {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Override
    public void run(String... args) throws Exception {

        boolean populated = true; // change if database is not populated = false

        if (!populated) {

            List<Theatre> theatres = populateTheatres();

            List<Movie> movies = populateMovies();

            populateShowtimes(movies, theatres);

            System.out.println("Database population completed successfully!");
        }
        System.out.println("Database already populated");
    }

    private List<Theatre> populateTheatres() {
        List<Theatre> theatres = new ArrayList<>();
        theatres.add(new Theatre(null, "Acmeplex Walid's Screen", "123 Golden Ridge Blvd, Northfield, Calgary",
                "/assets/theatres/acmeplex-walids-screen.jpg"));
        theatres.add(new Theatre(null, "Acmeplex Rak's Track", "456 Royal Plaza Rd, Riverside, Calgary",
                "/assets/theatres/acmeplex-raks-track.jpg"));
        theatres.add(new Theatre(null, "Acmeplex Straw's Show", "789 Silverstone Park Ave, Midtown, Calgary",
                "/assets/theatres/acmeplex-straws-show.jpg"));
        theatres.add(new Theatre(null, "Acmeplex Long's Song", "101 Luxe Towers, Downtown, Calgary",
                "/assets/theatres/acmeplex-longs-song.jpg"));

        return theatreRepository.saveAll(theatres);
    }

    private List<Movie> populateMovies() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("The Dark Knight", "Action", Date.valueOf("2024-10-01"),
                "A caped vigilante battles a criminal mastermind in Gotham.",
                9.0, 152, "/assets/movies/the-dark-knight.jpg", Movie.MovieStatus.AVAILABLE));

        movies.add(new Movie("The Shawshank Redemption", "Drama", Date.valueOf("2024-09-01"),
                "A man wrongly imprisoned bonds with a fellow inmate in a story of hope.",
                9.3, 142, "/assets/movies/shawshank-redemption.jpg", Movie.MovieStatus.AVAILABLE));

        movies.add(new Movie("The Grand Budapest Hotel", "Comedy", Date.valueOf("2024-10-21"),
                "A whimsical tale of a hotel concierge caught in a theft.",
                8.1, 99, "/assets/movies/grand-budapest-hotel.jpg", Movie.MovieStatus.AVAILABLE));

        movies.add(new Movie("Get Out", "Horror", Date.valueOf("2024-12-23"),
                "A visit to meet a girlfriend's family takes a dark turn.",
                8.7, 104, "/assets/movies/get-out.jpg", Movie.MovieStatus.AVAILABLE));

        movies.add(new Movie("Dune: Part Two", "Sci-Fi", Date.valueOf("2024-12-09"),
                "Paul Atreides unites with the Fremen to exact revenge against conspirators.",
                8.9, 155, "/assets/movies/dune-part-two.jpg", Movie.MovieStatus.AVAILABLE));

        movies.add(new Movie("The Marvels", "Action", Date.valueOf("2024-12-27"),
                "Carol Danvers teams up with Kamala Khan and Monica Rambeau to face a cosmic threat.",
                7.8, 130, "/assets/movies/the-marvels.jpg", Movie.MovieStatus.AVAILABLE));

        movies.add(new Movie("Inception", "Sci-Fi", Date.valueOf("2024-08-15"),
                "A skilled thief steals secrets through dream infiltration.",
                8.8, 148, "/assets/movies/inception.jpg", Movie.MovieStatus.AVAILABLE));

        movies.add(new Movie("Interstellar", "Sci-Fi", Date.valueOf("2024-07-20"),
                "A crew of astronauts travels through a wormhole to ensure humanity's survival.",
                8.6, 169, "/assets/movies/interstellar.jpg", Movie.MovieStatus.AVAILABLE));

        movies.add(new Movie("Parasite", "Thriller", Date.valueOf("2024-06-10"),
                "A struggling family schemes to infiltrate a wealthy household.",
                8.6, 132, "/assets/movies/parasite.jpg", Movie.MovieStatus.AVAILABLE));

        return movieRepository.saveAll(movies);
    }

    private void populateShowtimes(List<Movie> movies, List<Theatre> theatres) {
        List<Showtime> showtimes = new ArrayList<>();
        LocalDate startDate = LocalDate.now();
        Random random = new Random();

        for (Movie movie : movies) {
            LocalDate releaseDate = movie.getReleasedDate().toLocalDate();

            for (Theatre theatre : theatres) {

                LocalDate currentDate = releaseDate.isAfter(startDate) ? releaseDate : startDate;
                LocalDate endDate = currentDate.plusDays(6);

                while (!currentDate.isAfter(endDate)) {

                    int numberOfShowtimes = 2 + random.nextInt(3);

                    Set<Integer> hours = new HashSet<>();
                    while (hours.size() < numberOfShowtimes) {
                        int hour = 12 + random.nextInt(9);
                        hours.add(hour);
                    }

                    for (int hour : hours) {
                        showtimes.add(createShowtime(movie, theatre, currentDate,
                                Time.valueOf(String.format("%02d:00:00", hour))));
                    }

                    currentDate = currentDate.plusDays(1);
                }
            }
        }

        List<Showtime> savedShowtimes = showtimeRepository.saveAll(showtimes);

        populateSeats(savedShowtimes);
    }

    private Showtime createShowtime(Movie movie, Theatre theatre, LocalDate date, Time time) {
        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setTheatre(theatre);
        showtime.setShowDate(Date.valueOf(date));
        showtime.setShowTime(time);
        showtime.setCapacity(80);
        showtime.setBooked(false);

        return showtime;
    }

    private void populateSeats(List<Showtime> showtimes) {
        List<Seat> seats = new ArrayList<>();
        List<String> seatNumbers = SeatLayoutConfig.generateSeatNumbers();

        for (Showtime showtime : showtimes) {
            for (String seatNumber : seatNumbers) {
                Seat seat = new Seat();
                seat.setShowtime(showtime);
                seat.setSeatNumber(seatNumber);
                seat.setBooked(false);
                seat.setPrice(seatNumber);

                seats.add(seat);
            }
        }

        seatRepository.saveAll(seats);
    }

}
