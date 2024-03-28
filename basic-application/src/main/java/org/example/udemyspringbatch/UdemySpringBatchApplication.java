package org.example.udemyspringbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"org.example.udemyspringbatch.config", "org.example.udemyspringbatch.controller"})
public class UdemySpringBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(UdemySpringBatchApplication.class, args);
		System.out.println("This is a Test");
	}



}
