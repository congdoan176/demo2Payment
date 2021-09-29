package com.vnpay.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.vnpay")
public class PaymentApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PaymentApplication.class, args);
	}
	
}
