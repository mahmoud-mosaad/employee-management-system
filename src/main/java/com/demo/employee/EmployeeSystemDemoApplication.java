package com.demo.employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
@EnableScheduling
public class EmployeeSystemDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeSystemDemoApplication.class, args);
	}
}