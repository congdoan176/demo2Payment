package com.vnpay.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vnpay.controller.PaymentController;

//@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	private static Logger logger = LogManager.getLogger(PaymentController.class);
	  protected ResponseEntity<Object> handleMethodArgumentNotValid(
	      MethodArgumentNotValidException ex) {

	    Map<String, String> body = new HashMap<>();

	    List<String> errors = ex.getBindingResult()
	        .getFieldErrors()
	        .stream()
	        .map(DefaultMessageSourceResolvable::getDefaultMessage)
	        .collect(Collectors.toList());
	    	logger.error("ValidException: {}, Code respone: {}",errors, 01);
	    	body.put("code", String.valueOf(01));
	    	body.put("message", errors.toString());
	    
	    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	  }
}
