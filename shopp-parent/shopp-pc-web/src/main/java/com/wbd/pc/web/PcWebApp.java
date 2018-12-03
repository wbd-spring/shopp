package com.wbd.pc.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients //注入feign注解，实现客户端LB
public class PcWebApp {

	public static void main(String[] args) {
		
		SpringApplication.run(PcWebApp.class, args);

	}

}
