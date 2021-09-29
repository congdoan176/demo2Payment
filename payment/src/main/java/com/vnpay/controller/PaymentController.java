package com.vnpay.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vnpay.DTO.PaymentRequestApi;
import com.vnpay.DTO.Response;
import com.vnpay.service.PaymentService;

@RestController
public class PaymentController {
	@Autowired
	PaymentService paymentService;
	
	private static Logger logger = LogManager.getLogger(PaymentController.class);
	
	@PostMapping(value = "/add_payment",consumes = { "application/json" }, produces = { "application/json" })
	@ResponseBody
	public ResponseEntity<Response> addPayment(@Valid @RequestBody PaymentRequestApi paymentRequestApi){
		String tokenRequest = UUID.randomUUID().toString();
		ThreadContext.put("token", tokenRequest);
		Response paymentResponse = new Response();
		try {
			
			paymentResponse = paymentService.payment(paymentRequestApi);
			paymentResponse.setResponseId(tokenRequest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Payment service Exception:", e);
		} finally {
			ThreadContext.pop();
	        ThreadContext.clearAll();
		}
		return new ResponseEntity<>(paymentResponse,HttpStatus.OK);
	}
}
