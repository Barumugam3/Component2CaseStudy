package com.iiht.estock.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class StockCommandServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockCommandServiceApplication.class, args);
	}

}
