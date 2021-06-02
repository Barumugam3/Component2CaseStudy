package com.iiht.estock.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AuthMicroServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthMicroServiceApplication.class, args);
	}

}
