package com.vnpay.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnpay.DTO.PaymentRequestApi;
import com.vnpay.DTO.Response;
import com.vnpay.config.YamlBankProperties;
import com.vnpay.model.Bank;
import com.vnpay.util.Enums;
import com.vnpay.util.JedisUtil;
import com.vnpay.util.MapperObject;
import com.vnpay.util.Sha256Hmac;

@Service
public class PaymentService {
	@Autowired
	JedisUtil jedisUtil;
	@Autowired
	YamlBankProperties yamlBankProperties;
	@Autowired
	private AmqpTemplate rabbitTemplate;
	@Value("${vnpay.rabbitmq.exchange}")
	private String exchange;
	
	@Value("${vnpay.rabbitmq.routingkey}")
	private String routingkey;  
	
	private static Logger logger = LogManager.getLogger(PaymentService.class);
	
	public Response payment(PaymentRequestApi paymentRequestApi) throws Exception {
		logger.info("Begin payment: {}", paymentRequestApi);
		// check token not matching
		if(findTokenKey(paymentRequestApi.getTokenKey())) {
			logger.info("Token key exists: {}", paymentRequestApi.getTokenKey());
			return new Response(Enums.status.tokenKeyExist.getStatus(),"token key already exists");
		}
		Bank bank = findBankCode(paymentRequestApi.getBankCode());
		if(null == bank) {
			return new Response(Enums.status.bankCodeNotExist.getStatus(),"Bank code does not exist");
		}
		// validate payDate
		String payDate = paymentRequestApi.getPayDate();
		if(!checkPayDate(payDate)) {
			return new Response(Enums.status.validateFiledError.getStatus(),"Filed payDate malformed or expired");
		}
		// check Sum
		if(!checkSum(paymentRequestApi,bank.getPrivateKey())) {
			return new Response(Enums.status.checkSumError.getStatus(),"CheckSum incorrect");
		}
		// save redis
		if(!saveDataToRedis(paymentRequestApi)) {
			 new Response(Enums.status.saveDataRedisError.getStatus(), "Server error");
		}
		
		// send message to rabbit
		String mesageCode = sendQueue(paymentRequestApi);
		if("200".equals(mesageCode)) {
			return new Response(Enums.status.success.getStatus(),"success");
		}else {
			return new Response(Enums.status.transactionError.getStatus(),"Transaction error");
		}
	}
	
	/* send message to rabbitmq .
	 * input data request*/
	public String sendQueue(PaymentRequestApi paymentRequestApi) throws JsonProcessingException, AmqpException {
		logger.info("Begin data to rabbit: {}", paymentRequestApi);
		String messsage = (String) rabbitTemplate.convertSendAndReceive(exchange, routingkey,MapperObject.getMapperObject().objectToJson(paymentRequestApi));
		logger.info("Response from rabbit: {}", messsage);
		return messsage;
	}

	/* check bank code
	 * input is bank code*/
	private Bank findBankCode(String bankCode) {
		logger.info("Check bank code: {}", bankCode);
		List<Bank> listBank = yamlBankProperties.getBanks();
		Bank bank = null;
		 if(listBank.stream().filter(b -> b.getBankCode().equals(bankCode)).collect(Collectors.toList()).size() > 0) {
			 bank = listBank.stream().filter(b -> b.getBankCode().equals(bankCode)).collect(Collectors.toList()).get(0);
		 }else {
			 logger.info("Bank code does not exist: {}", bankCode);
		 }
		 return bank;
	}

	/* input is request data and private key.
	 *  to perform hashing. */
	public boolean checkSum(PaymentRequestApi paymentRequest, String privateKey) throws Exception {
		logger.info("Check sum data request: {} , Private key: {}", paymentRequest, privateKey);
		String hmacCheckSum = paymentRequest.getMobile() + paymentRequest.getBankCode() + paymentRequest.getAccountNo()
				+ paymentRequest.getPayDate() + paymentRequest.getDebitAmount() + paymentRequest.getRespCode()
				+ paymentRequest.getTraceTransfer() + paymentRequest.getMessageType() + privateKey;
		logger.info("Hmac String: {}", hmacCheckSum);
		String encodeCheckSum = "";
		try {
			 encodeCheckSum = Sha256Hmac.encode(hmacCheckSum, privateKey);
		} catch (Exception e) {
			logger.error("Encode hmacCheckSum error: {}", e);
			return false;
			// TODO: handle exception
		}
		logger.info("Encode hmacCheckSum: {}", encodeCheckSum);
		if(encodeCheckSum.equals(paymentRequest.getCheckSum())) {
			return true;
		}
		return false;
	}

	private boolean findTokenKey(String tokenKey) {
		return jedisUtil.exits(tokenKey);
	}
	
	private boolean checkPayDate(String payDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		// get time now
		Long dateNow = Calendar.getInstance().getTimeInMillis();
		try {
			Date converDate = format.parse(payDate);
			if((Math.abs(dateNow - converDate.getTime())/1000) < 300) {
				return true;
			}else {
				return false;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	private boolean saveDataToRedis(PaymentRequestApi paymentRequestApi) {
		try {
			jedisUtil.save(paymentRequestApi.getTokenKey(), paymentRequestApi.toString());
			Long timeExpire = Date.from(LocalDateTime.now().with(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()).getTime();
			jedisUtil.expire(paymentRequestApi.getTokenKey(), timeExpire);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Save Data Redis: ", e);
			return false;
		}
		
	}
}
