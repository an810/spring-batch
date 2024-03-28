package org.example.chunkprocessing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.example.chunkprocessing.config", "org.example.chunkprocessing.controller"})
public class ChunkProcessingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChunkProcessingApplication.class, args);
		System.out.println("This is a Test");
	}

}
