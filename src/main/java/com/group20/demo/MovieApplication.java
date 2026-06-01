package com.group20.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@ComponentScan(basePackages = "com.group20") // Ensures @Controller, @Service, etc., are detected
@EnableJpaRepositories(basePackages = "com.group20.Repository") // Detects @Repository
@EntityScan(basePackages = "com.group20.model") // Detects @Entity
@EnableScheduling // Enables scheduled tasks
public class MovieApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieApplication.class, args);
	}

}
