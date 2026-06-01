package com.group20.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group20.Strategy.Payment;
import com.group20.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("Select u.payment FROM User u where u.id = :userId")
	Payment findPaymentByUserId(@Param("userId") Long userId);

	@Query("Select u.firstName FROM User u where u.id = :userId")
	String findFirstNameByUserId(@Param("userId") Long userId);

	@Query("SELECT u.lastName FROM User u WHERE u.id = :userId")
	String findLastNameByUserId(@Param("userId") Long userId);

	User findByEmail(String email);

	@Query("SELECT u.email FROM User u WHERE u.id = :userId")
	String findEmailByUserId(@Param("userId") Long userId);

	@Query("SELECT u.payment FROM User u WHERE u.email = :email")
	String findPaymentByEmail(@Param("email") String email);

	List<User> findByRenewal(Date renewalDate);

	boolean existsByEmail(String email);

}
