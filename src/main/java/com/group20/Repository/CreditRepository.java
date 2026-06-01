package com.group20.Repository;

import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.group20.model.Credit;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {
	
	@Query("Select c.expiry FROM Credit c WHERE c.id = :creditId")
	Date findExpiryByCreditId(@Param("creditId") Long creditId);
	
	@Query("SELECT c.amount FROM Credit c WHERE c.id = :creditId")
    double findAmountByCreditId(@Param("creditId") Long creditId);
	
	boolean existsById(Long creditId);

}
