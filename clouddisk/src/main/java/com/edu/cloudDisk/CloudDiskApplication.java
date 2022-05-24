package com.edu.cloudDisk;

import com.lingoace.starter.alive.enable.EnableAlive;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableAlive
@EnableFeignClients(basePackages = {"com.lingoace.*"})
public class CloudDiskApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudDiskApplication.class, args);
    }
}
