package com.group20.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.group20.model.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

        @Query("SELECT s.booked FROM Seat s WHERE s.seatNumber = :seatNumber AND s.showtime.id = :showtimeId")
        Boolean findBookedBySeatNumberAndShowtime(@Param("seatNumber") String seatNumber,
                        @Param("showtimeId") Long showtimeId);

        @Query("SELECT s.seatNumber FROM Seat s WHERE s.showtime.id = :showtimeId")
        List<String> findSeatNumbersByShowtime(@Param("showtimeId") Long showtimeId);

        List<Seat> findBySeatNumberAndBookedAndShowtime_Id(String seatNumber, boolean booked, Long showtimeId);

        @Query("SELECT s.seatNumber FROM Seat s WHERE s.booked = :booked AND s.showtime.id = :showtimeId")
        List<String> findSeatNumbersByShowtimeAndBooked(@Param("showtimeId") Long showtimeId,
                        @Param("booked") boolean booked);

        @Query("SELECT s FROM Seat s WHERE s.showtime.id = :showtimeId AND s.seatNumber = :seatNumber")
        Seat findByShowtimeIdAndSeatNumber(@Param("showtimeId") Long showtimeId,
                        @Param("seatNumber") String seatNumber);

        @Query("SELECT s FROM Seat s WHERE s.showtime.id = :showtimeId")
        List<Seat> findSeatsByShowtimeId(@Param("showtimeId") Long showtimeId);

        @Query("SELECT s.seatNumber FROM Seat s WHERE s.id = :seatId")
        String findSeatNumberBySeatId(@Param("seatId") Long seatId);

}
