package com.movie.showtimeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ShowtimeserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShowtimeserviceApplication.class, args);
	}

}
