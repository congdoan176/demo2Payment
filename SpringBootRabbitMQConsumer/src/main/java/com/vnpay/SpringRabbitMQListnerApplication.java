package com.vnpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.vnpay")
@EnableJpaRepositories("com.vnpay.repository")
public class SpringRabbitMQListnerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRabbitMQListnerApplication.class, args);
	}
}
