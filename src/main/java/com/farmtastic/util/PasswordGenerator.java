package com.farmtastic.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordGenerator {
	public static void main(String[] args) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String rawPassword = "abc1477383";
		String encodedPassword = encoder.encode(rawPassword);
		System.out.println("加密後密碼: " + encodedPassword);
	}
}
