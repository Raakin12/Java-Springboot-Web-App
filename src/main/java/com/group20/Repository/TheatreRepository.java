package com.group20.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.group20.model.Theatre;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {
	
	@Query("SELECT t.id FROM Theatre t WHERE t.name = :name")
    Long findIdByName(@Param("name") String name);
	
	@Query("SELECT t.name FROM Theatre t WHERE t.id = :id")
	String findNameById(@Param("id") Long id);
	
}
