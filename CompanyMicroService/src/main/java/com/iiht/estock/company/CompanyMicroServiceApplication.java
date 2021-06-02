package com.iiht.estock.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CompanyMicroServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompanyMicroServiceApplication.class, args);
    }

}
