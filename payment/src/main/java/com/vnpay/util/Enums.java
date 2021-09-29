package com.vnpay.util;

public class Enums {
	
	public static enum status {
		success("00"),transactionError("-1"), bankCodeNotExist("02"), checkSumError("03"), validateFiledError("04"), saveDataRedisSuccess("01"), saveDataRedisError("05"),
		sendMessageRabbitSuccess("1"), sendMessageRabbitError("07"),tokenKeyExist("08");
		private String status;

		private status(String status) {
			this.status = status;
		}

		public String getStatus() {
			return status;
		}
	}
}
