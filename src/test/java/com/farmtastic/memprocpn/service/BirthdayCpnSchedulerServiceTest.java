package com.farmtastic.memprocpn.service;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.farmtastic.common.constants.CpnConstants;
import com.farmtastic.member.model.Mem;
import com.farmtastic.member.model.MemRepository;
import com.farmtastic.memprocpn.model.BirthdayCpnSchedulerService;
import com.farmtastic.memprocpn.model.MemProCpnServiceImp;

@ExtendWith(MockitoExtension.class)
class BirthdayCpnSchedulerServiceTest {

	@Mock
	private MemRepository memRepository;

	@Mock
	private MemProCpnServiceImp memProCpnService;

	@InjectMocks
	private BirthdayCpnSchedulerService birthdayCpnSchedulerService;

	@Test
	void testSendBirthdayCoupons_withMembers() {
		// 模擬今天生日的會員
		Mem mem = new Mem();
		mem.setMemId(1);
		List<Mem> mockMembers = List.of(mem);

		// 模擬 Repository 回傳一筆壽星
		when(memRepository.findByBirthday(anyInt(), anyInt())).thenReturn(mockMembers);

		// 執行測試
		birthdayCpnSchedulerService.sendBirthdayCoupons();

		// 驗證確實有呼叫發券方法
		verify(memProCpnService, times(1)).giveCoupon(eq(1), eq(CpnConstants.BIRTHDAY_COUPON_ID));
	}

	@Test
	void testSendBirthdayCoupons_noMembers() {
		// 模擬今天沒有壽星
		when(memRepository.findByBirthday(anyInt(), anyInt())).thenReturn(Collections.emptyList());

		// 執行測試（預期不會呼叫發券）
		birthdayCpnSchedulerService.sendBirthdayCoupons();

		// 驗證沒有呼叫發券
		verify(memProCpnService, never()).giveCoupon(anyInt(), anyInt());
	}
}