package com.group20.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.group20.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

	@Query("SELECT m FROM Movie m WHERE m.releasedDate < :date")
	List<Movie> findMoviesByReleasedDateBefore(@Param("date") Date date);

	@Query("SELECT m.title FROM Movie m WHERE m.id = :id")
	String findNameById(@Param("id") Long id);

	List<Movie> findByStatus(Movie.MovieStatus status);

}
