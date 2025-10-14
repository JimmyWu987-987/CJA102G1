package com.farmtastic.memprocpn.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.farmtastic.memprocpn.model.MemProCpnServiceImp;
import com.farmtastic.proorder.model.ProOrderRepository;
import com.farmtastic.proorder.model.ProOrderVO;

@SpringBootTest
public class MemProCpnServiceTest {

	@Autowired
	private MemProCpnServiceImp memProCpnService;
	@Autowired
	private ProOrderRepository proOrderRepository;

	@Test
	public void testChangeCouponStatus() {
		ProOrderVO order = proOrderRepository.findById(5).orElseThrow();

		memProCpnService.changeMemProCpnStatus(1, 2);

		System.out.println("✅ 測試成功，折價券已綁定訂單 " + order.getProOrdId());
	}

	@Test
	public void testGiveBirthdayCoupons() {
		// memProCpnService.giveBirthdayCoupons();
	}
}
