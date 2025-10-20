package com.farmtastic.memprocpn.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.farmtastic.procpn.model.ProCpnVO;

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

	/**
	 * 取得目前抽獎券清單（供前端初始化 flowerMap）
	 */
	@GetMapping("/list")
	@ResponseBody
	public List<Map<String, Object>> getLotteryCoupons() {
		// 從 service 層取得抽獎券池
		List<ProCpnVO> lotteryCoupons = spinService.getLotteryCoupons();

		return lotteryCoupons.stream().map(c -> {
			Map<String, Object> item = new HashMap<>();
			item.put("name", c.getCpnName());
			item.put("discValue", c.getDiscValue());
			item.put("desc", c.getCpnDesc());
			item.put("validDays", c.getValidDays());
			return item;
		}).collect(Collectors.toList());
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
