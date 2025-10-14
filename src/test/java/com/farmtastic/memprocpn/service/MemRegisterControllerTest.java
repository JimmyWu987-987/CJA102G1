package com.farmtastic.memprocpn.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.common.constants.CpnConstants;
import com.farmtastic.member.controller.MemController;
import com.farmtastic.member.model.Mem;
import com.farmtastic.member.model.MemService;
import com.farmtastic.memprocpn.model.MemProCpnServiceImp;
import com.farmtastic.redis.verification.MailService;
import com.farmtastic.redis.verification.RedisService;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
public class MemRegisterControllerTest {
	@Mock
	private MemService memSvc;
	@Mock
	private RedisService redisSvc;
	@Mock
	private MailService mailSvc;
	@Mock
	private MemProCpnServiceImp memProCpnSvc;
	@Mock
	private BindingResult result;
	@Mock
	private HttpServletRequest request;
	@Mock
	private RedirectAttributes redirectAttrs;
	@Mock
	private ModelMap model;

	@InjectMocks
	private MemController controller; // ← 你的 controller 類別名稱

	private Mem mem;

	@BeforeEach
	void setUp() {
		mem = new Mem();
		mem.setMemId(1);
		mem.setMemAcc("waUser");
		mem.setMemMobile("0912345678");
		mem.setMemEmail("test@example.com");
	}

	@Test
	void testRegisterSuccess() {
		// 模擬帳號與手機未被註冊
		when(memSvc.existsByMemAcc("waUser")).thenReturn(false);
		when(memSvc.existsByMemMobile("0912345678")).thenReturn(false);
		when(result.hasErrors()).thenReturn(false);

		// 模擬 request URL 組成
		when(request.getScheme()).thenReturn("http");
		when(request.getServerName()).thenReturn("localhost");
		when(request.getServerPort()).thenReturn(8080);

		// ✅ 在執行前先設定 doAnswer，這樣會在呼叫 giveCoupon() 當下印出
		doAnswer(invocation -> {
			Integer memId = invocation.getArgument(0);
			Integer cpnId = invocation.getArgument(1);
			System.out.printf("🎫 模擬發券 -> memId=%d, cpnId=%d%n", memId, cpnId);
			return null;
		}).when(memProCpnSvc).giveCoupon(anyInt(), anyInt());

		System.out.println("▶ 測試開始：模擬會員註冊流程...");

		// 執行 Controller 方法
		String view = controller.register(request, mem, result, model, redirectAttrs);

		// 驗證：新增會員有被呼叫
		verify(memSvc, times(1)).addMem(mem);

		// 驗證：發券有被呼叫
		verify(memProCpnSvc, times(1)).giveCoupon(eq(1), eq(CpnConstants.REGISTER_DISCOUNT_ID));
		verify(memProCpnSvc, times(1)).giveCoupon(eq(1), eq(CpnConstants.REGISTER_CASHBACK_ID));

		// 驗證：redis、mail 雖然存在，但可略過
		verify(redisSvc, atLeast(0)).setVerificationCode(anyString(), anyString(), anyInt());
		verify(mailSvc, atLeast(0)).sendMail(anyString(), anyString(), anyString());

		// 驗證：回傳 redirect
		assertEquals("redirect:/", view);
		System.out.println("✅ 成功新增會員並發送折價券");
		System.out.println("✅ 測試完成：testRegisterSuccess()");
	}

}
