package com.example.fileupload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FileUpLoadApplication {
	public static void main(String[] args) {
		SpringApplication.run(FileUpLoadApplication.class, args);
	}

}
