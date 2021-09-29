package com.vnpay.DTO;

import lombok.Data;

@Data
public class Response {
	String code;
	String message;
	String responseId;
	String checkSum;
	String addValue;
	
	
	@Override
	public String toString() {
		return "Response [code=" + code + ", message=" + message + ", responseId=" + responseId + ", checkSum="
				+ checkSum + ", addValue=" + addValue + "]";
	}


	public Response(String code, String message, String responseId, String checkSum, String addValue) {
		super();
		this.code = code;
		this.message = message;
		this.responseId = responseId;
		this.checkSum = checkSum;
		this.addValue = addValue;
	}
	

	public Response() {
		super();
	}


	public Response(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	
}
