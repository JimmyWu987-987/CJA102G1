package com.farmtastic.memprocpn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.farmtastic.member.model.Mem;
import com.farmtastic.memprocpn.model.SpinServiceImp;

import jakarta.servlet.http.HttpSession;

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
	public ResponseEntity<String> spinCoupon(HttpSession session, Model model) {
		Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");
		Integer memId = loggedInMember.getMemId();
		String result = spinService.spinAndGiveCoupon(memId);
		return ResponseEntity.ok(result);
	}
}
