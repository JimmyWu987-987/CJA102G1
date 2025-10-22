package com.farmtastic.couponlog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.farmtastic.couponlog.model.CouponLogService;
import com.farmtastic.couponlog.model.CouponLogVO;

@Controller
@RequestMapping("/admin/log")
public class CouponLogController {

	private final CouponLogService logService;

	private static final String LOG_PATH = "back_end/logined/log/couponLogList";

	@Autowired
	public CouponLogController(CouponLogService logService) {
		this.logService = logService;
	}

	// 顯示所有操作日誌
	@GetMapping("/list")
	public String listAllLogs(Model model) {
		List<CouponLogVO> logs = logService.findAllLogs();
		model.addAttribute("logs", logs);
		return LOG_PATH; // 對應 Thymeleaf 頁面
	}

	// 根據管理員 ID 篩選操作紀錄
	@GetMapping("/byAdmin")
	public String listLogsByAdmin(@RequestParam("adminId") Integer adminId, Model model) {
		List<CouponLogVO> logs = logService.findLogsByAdmin(adminId);
		model.addAttribute("logs", logs);
		model.addAttribute("filterType", "admin");
		model.addAttribute("filterValue", adminId);
		return LOG_PATH;
	}

	// 根據折價券 ID 篩選操作紀錄
	@GetMapping("/byCoupon")
	public String listLogsByCoupon(@RequestParam("proCpnId") Integer proCpnId, Model model) {
		List<CouponLogVO> logs = logService.findLogsByCoupon(proCpnId);
		model.addAttribute("logs", logs);
		model.addAttribute("filterType", "coupon");
		model.addAttribute("filterValue", proCpnId);
		return LOG_PATH;
	}
}
