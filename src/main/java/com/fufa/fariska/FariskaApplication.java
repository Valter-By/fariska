package com.fufa.fariska;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.fufa")
@EnableAsync
public class FariskaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FariskaApplication.class, args);
	}

}
