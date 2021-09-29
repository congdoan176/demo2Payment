package com.vnpay.model;

import lombok.Data;

@Data
public class Bank {
	private String bankCode;
	private String privateKey;
	private String ips;
	
	@Override
	public String toString() {
		return "Bank [bankCode=" + bankCode + ", privateKey=" + privateKey + ", ips=" + ips + "]";
	}
}
