package com.vnpay.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;
	@NotNull
	String tokenKey;
	@NotNull
	String apiID;
	@NotNull
	String mobile;
	@NotNull
	String bankCode;
	@NotNull
	String accountNo;
	@NotNull
	String payDate;
	@NotNull
	String additionalData;
	@NotNull
	long debitAmount;
	@NotNull
	String respCode;
	@NotNull
	String respDesc;
	@NotNull
	String traceTransfer;
	@NotNull
	String messageType;
	@NotNull
	String checkSum;
	@NotNull
	String orderCode;
	@NotNull
	String userName;
	@NotNull(message = "realAmount not Null")
	long realAmount;
	@NotNull(message = "Promotion Code Null")
	@NotBlank(message = "Promotion Code Blank")
	String promotionCode;
	
	String addValue;
}
