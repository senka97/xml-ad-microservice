package com.team19.admicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableFeignClients
public class AdmicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdmicroserviceApplication.class, args);
    }

}
