package com.vnpay.service;

import java.io.IOException;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.vnpay.model.Payment;
import com.vnpay.repository.PaymentRepository;
import com.vnpay.util.MapperObject;

@Component
public class RabbitMQConsumer {
	@Autowired
	PaymentRepository paymentRepository;
	private static Logger logger = LogManager.getLogger(RabbitMQConsumer.class);
	@Value("${vnpay.server.url}")
	String url;
	@RabbitListener(queues = "${vnpay.rabbitmq.queue}")
	public String recievedMessage(Message message) {
		String tokenRequest = UUID.randomUUID().toString();
		ThreadContext.put("token", tokenRequest);
		String queueData = new String(message.getBody());
		try {
			logger.info("Queue data request: {}", queueData);
			// convert queueData to object
			Payment payment = MapperObject.getMapperObject().toEntity(queueData);
			// save queue data to database
			paymentRepository.save(payment);
			logger.info("Save payment success: {}", "200");
			// send data to server
			ResponseEntity<?> result =sendToServer(payment, url);
			logger.info("Respone to server: ", result);
			if(200 == result.getStatusCodeValue()) {
				return String.valueOf(result.getStatusCodeValue());
			}
			return "";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Process queue exception: ", e);
			return "";
		} finally {
			ThreadContext.pop();
	        ThreadContext.clearAll();
		}
	}
	private ResponseEntity<?> sendToServer(Payment payment, String url){
		logger.info("Begin to server: {} and url: {}", payment, url);
		try {
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Object> responseEntity = restTemplate.postForEntity(url, payment, Object.class);
			return responseEntity;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Send to server: ", e);
			return null;
		}
	}
	
}