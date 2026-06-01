package com.group20.Repository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.group20.model.Showtime;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

	List<Showtime> findByTheatreIdAndMovieId(Long theatreId, Long movieId);

	@Query("SELECT s.showTime FROM Showtime s WHERE s.id = :showId")
	Time findShowTimeByShowId(@Param("showId") Long showId);

	@Query("SELECT s FROM Showtime s WHERE s.showDate <= :currentDate AND s.showTime <= :currentTime")
	List<Showtime> findExpiredShowtimes(@Param("currentDate") Date currentDate, @Param("currentTime") Time currentTime);

	@Modifying
	@Query("UPDATE Showtime s SET s.capacity = s.capacity + 1 WHERE s.id = :showtimeId AND s.capacity < s.maxCapacity")
	int incrementCapacity(@Param("showtimeId") Long showtimeId);

}
