package com.applause.urlshortner;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This is driver class that starts the program.
 */
@SpringBootApplication
public class Application {

	/**
	 * This is the function that starts the program.
	 * @param args argument passed through command line
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
