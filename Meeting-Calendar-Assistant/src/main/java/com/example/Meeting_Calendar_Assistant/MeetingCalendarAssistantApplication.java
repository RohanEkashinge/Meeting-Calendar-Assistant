package com.example.Meeting_Calendar_Assistant;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Meeting Calendar Assistant application.
 *
 * This application handles meeting scheduling and employee management.
 * It loads configuration from a .env file and initializes the Spring application context.
 */
@SpringBootApplication
public class MeetingCalendarAssistantApplication {

	private static final Logger logger = LoggerFactory.getLogger(MeetingCalendarAssistantApplication.class);

	/**
	 * Main method to start the application.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {

		// Load the .env file from the root directory
		Dotenv dotenv = Dotenv.configure()
				.directory("path to the .env file")  // Full path to the project root
				.load();

		// Log database connection properties (without sensitive data)
		logger.info("Database URL: {}", dotenv.get("SPRING_DATASOURCE_URL"));
		logger.info("Database Username: {}", dotenv.get("SPRING_DATASOURCE_USERNAME"));

		System.setProperty("spring.datasource.url", dotenv.get("SPRING_DATASOURCE_URL"));
		System.setProperty("spring.datasource.username", dotenv.get("SPRING_DATASOURCE_USERNAME"));
		System.setProperty("spring.datasource.password", dotenv.get("SPRING_DATASOURCE_PASSWORD"));

		logger.info("Starting Meeting Calendar Assistant Application...");

		SpringApplication.run(MeetingCalendarAssistantApplication.class, args);

		logger.info("Application started successfully.");

	}

}
