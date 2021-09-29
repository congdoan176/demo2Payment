package com.vnpay.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapperObject {

	private ObjectMapper objectMapper;

	private MapperObject() {
		objectMapper = new ObjectMapper();
	}

	public static MapperObject getMapperObject() {
		return new MapperObject();
	}

	public String objectToJson(Object object) throws JsonProcessingException {
		return this.objectMapper.writeValueAsString(object);
	}
}
