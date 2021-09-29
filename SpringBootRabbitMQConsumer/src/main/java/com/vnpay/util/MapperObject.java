package com.vnpay.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnpay.model.Payment;

public class MapperObject {

	private ObjectMapper objectMapper;

	private MapperObject() {
		objectMapper = new ObjectMapper();
	}

	public static MapperObject getMapperObject() {
		return new MapperObject();
	} {
		
	}
	public Payment toEntity(String jsonObject) throws JsonParseException, JsonMappingException, IOException {
		return this.objectMapper.readValue(jsonObject, Payment.class);
	}
}
