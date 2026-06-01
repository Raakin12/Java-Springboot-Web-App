package com.group20.Repository;
import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.group20.model.Booking;
import com.group20.model.Seat;
import com.group20.model.Showtime;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>{

	@Query("SELECT b.bookingDate FROM Booking b WHERE b.id = :bookingId")
    Date findBookingDateById(@Param("bookingId") Long bookingId);
	
	@Query("SELECT b.seat FROM Booking b WHERE b.id = :bookingId")
	Seat findSeatByBookingId(@Param("bookingId") Long bookingId);
	
	@Query("SELECT b.showtime FROM Booking b WHERE b.id = :bookingId")
	Showtime findShowtimeByBookingId(@Param("bookingId") Long bookingId);

	boolean existsById(Long bookingId);
	
}
