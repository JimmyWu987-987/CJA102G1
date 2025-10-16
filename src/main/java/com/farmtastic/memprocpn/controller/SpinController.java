package com.farmtastic.memprocpn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.farmtastic.memprocpn.model.SpinServiceImp;

@Controller
@RequestMapping("/mem/spin")
public class SpinController {
	@Autowired
	private SpinServiceImp spinService;

	// 顯示轉盤頁面
	@GetMapping
	public String showSpinPage() {
		System.out.println("✅ 已進入 showSpinPage()");
		return "front_end/customer/logined/memcpn/Spin";
	}

	// 抽獎發券 API
	@PostMapping("/coupons")
	public ResponseEntity<String> spinCoupon(@RequestParam Integer memId) {
		String result = spinService.spinAndGiveCoupon(memId);
		return ResponseEntity.ok(result);
	}
}
