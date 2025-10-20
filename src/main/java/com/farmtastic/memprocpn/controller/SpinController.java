package com.farmtastic.memprocpn.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
		return "front_end/customer/logined/memcpn/Spin";
	}

	// 抽獎發券 API
	@PostMapping("/coupons")
	@ResponseBody
	// Spring 的 HTTP 回應包裝器，泛型代表回傳內容型別是字串
	public ResponseEntity<Map<String, Object>> spinCoupon(HttpSession session) {
		// 這登入時放的會員物件， Session
		Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");
		if (loggedInMember == null) {
			Map<String, Object> error = new HashMap<>();
			error.put("status", "UNAUTHORIZED");
			error.put("message", "請先登入會員才能抽籤！");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}
		// 取出會員ID
		Integer memId = loggedInMember.getMemId();
		// 呼叫 Service 層邏輯，執行抽活動折價券
		// 回傳結果字串
		Map<String, Object> result = spinService.spinAndGiveCoupon(memId);
		// 使用 ResponseEntity 包裝 HTTP 回應
		return ResponseEntity.ok(result);
	}
}
