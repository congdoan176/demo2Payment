package com.vnpay.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.vnpay.model.Bank;

import lombok.Data;
@Configuration
@ConfigurationProperties
@PropertySource(value = "classpath:banks.yml", factory = YamlPropertySourceFactory.class)
@Data
public class YamlBankProperties {
	private List<Bank> banks;
	@Override
	public String toString() {
		return "YamlBankProperties [banks=" + banks + "]";
	}
	
	
}
