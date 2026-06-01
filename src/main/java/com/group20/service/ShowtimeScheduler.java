package com.group20.service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.group20.Repository.ShowtimeRepository;
import com.group20.Strategy.ShowtimeObserver;
import com.group20.model.Showtime;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class ShowtimeScheduler {

	private final ShowtimeRepository showtimeRepository;
    private final List<ShowtimeObserver> showtimeObservers;

    public ShowtimeScheduler(ShowtimeRepository showtimeRepository, List<ShowtimeObserver> showtimeObservers) {
        this.showtimeRepository = showtimeRepository;
        this.showtimeObservers = showtimeObservers;
    }

    @Scheduled(fixedRate = 60000)
    public void checkAndResetExpiredShowtimes() {

        Time currentTime = Time.valueOf(LocalTime.now());
        Date currentDate = Date.valueOf(LocalDate.now());

        List<Showtime> expiredShowtimes = showtimeRepository.findExpiredShowtimes(currentDate,currentTime);

        for (Showtime showtime : expiredShowtimes) {
            resetShowtime(showtime.getId());
        }
    }

    private void resetShowtime(Long showtimeId) {
        for (ShowtimeObserver observer : showtimeObservers) {
            observer.update(showtimeId);
        }
    }
}
