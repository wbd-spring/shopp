package com.wbd.eureka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer//eureka server 端
public class StartServer {

	public static void main(String[] args) {

		SpringApplication.run(StartServer.class, args);

	}

}
