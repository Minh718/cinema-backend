package com.movie.heatseatservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableDiscoveryClient
@EnableWebSocket
public class HeatseatserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HeatseatserviceApplication.class, args);
	}

}
