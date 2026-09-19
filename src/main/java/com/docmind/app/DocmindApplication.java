package com.docmind.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DocmindApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocmindApplication.class, args);
	}

}
