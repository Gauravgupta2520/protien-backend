package com.example.protien;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ProtienApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProtienApplication.class, args);
	}

}
