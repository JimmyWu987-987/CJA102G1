package com.farmtastic.memprocpn.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.farmtastic.memprocpn.model.MemProCpnServiceImp;

@SpringBootTest
public class MemProCpnServiceTest {

	@Autowired
	private MemProCpnServiceImp memProCpnService;

	@Test
	public void testGiveBirthdayCoupons() {
		// memProCpnService.giveBirthdayCoupons();
	}
}
