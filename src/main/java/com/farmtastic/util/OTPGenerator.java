package com.farmtastic.util;

import java.security.SecureRandom;

public class OTPGenerator {
	private static final SecureRandom secureRandom = new SecureRandom();
	
	public static String generateOTP() {
		int otp = 100000 + secureRandom.nextInt(900000);
							// 會回傳一個 0 ~ 899999 的隨機整數
		return String.valueOf(otp);
	}
	
//	public static void main(String[] args) {
//		System.out.println(generateOTP());
//	}
}
