package com.group20.model;

import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

import java.sql.Date;
import java.util.Random;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Credit {

	@Id
	@Column(name = "credit_id", nullable = false, unique = true)
	private Long Id;

	@Column(nullable = false)
	private Date expiry;

	private double amount;

	public Credit() {
	}

	public Credit(Long id, Date expiry, double amount) {
		super();
		Id = id;
		this.expiry = expiry;
		this.amount = amount;
	}

	@PrePersist
	private void generateId() {
		if (this.Id == null) {
			this.Id = generateRandomLongId();
		}
	}

	private Long generateRandomLongId() {
		Random random = new Random();
		long lowerBound = 1_000_000_000_000_000L;
		long upperBound = 9_999_999_999_999_999L;
		return lowerBound + (long) (random.nextDouble() * (upperBound - lowerBound));
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
