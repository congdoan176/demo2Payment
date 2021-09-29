package com.vnpay.DTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;

import lombok.Data;

@Data
public class PaymentRequestApi {
	@NotNull(message = "TokenKey not null")
	String tokenKey;
	@NotNull(message = "apiId not null")
	String apiID;
	@NotNull(message = "mobile not null")
	String mobile;
	@NotNull(message = "bankCode not null")
	String bankCode;
	@NotNull(message = "accountNo not null")
	String accountNo;
	@NotNull(message = "payDate not null")
	String payDate;
	@NotNull(message = "additionalData not null")
	String additionalData;
	@NotNull(message = "debitAmount not null")
	String debitAmount;
	@NotNull(message = "respCode not null")
	String respCode;
	@NotNull(message = "respDesc not null")
	String respDesc;
	@NotNull(message = "traceTransfer not null")
	String traceTransfer;
	@NotNull(message = "messageType not null")
	String messageType;
	@NotNull(message = "checkSum not null")
	String checkSum;
	@NotNull(message = "orderCode not null")
	String orderCode;
	@NotNull(message = "userName not null")
	String userName;
	@NotNull(message = "realAmount not Null")
	String realAmount;
	String promotionCode;
	@Value("\"{\\\"payMethod\\\":\\\"01\\\",\\\"payMethodMMS\\\":1}\"")
	String addValue;

}
