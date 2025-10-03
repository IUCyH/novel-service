package com.iucyh.novelservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NovelServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NovelServiceApplication.class, args);
	}

}
