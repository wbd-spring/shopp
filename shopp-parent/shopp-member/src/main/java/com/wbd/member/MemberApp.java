package com.wbd.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient //eureka客户端
@ComponentScan(basePackages={"com.wbd"})//扫描com.wbd(包含comm项目下的对象)
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class MemberApp {

	public static void main(String[] args) {
		
		SpringApplication.run(MemberApp.class, args);
	}

}
