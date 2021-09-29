package com.vnpay.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.vnpay.DTO.PaymentRequestApi;
import com.vnpay.controller.PaymentController;

@Component
public class Validate {
	
	private static Logger logger = LogManager.getLogger(Validate.class);
	
	public static String validate(PaymentRequestApi paymentRequestApi) {
		logger.info("Validate realAmount: {} , ", paymentRequestApi.getRealAmount());
		
		return "";
	}
}
