package com.farmtastic.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 折價券總管控制器 負責統一導向「活動券、商品券、明細」四大後台模組
 */
@Controller
@RequestMapping("/admin/coupon")
public class AdminCouponController {
	/**
	 * 折價券首頁 導向四個子模組的入口 URL: /admin/coupon/manage
	 */
	@GetMapping("/manage")
	public String showCouponManagePage() {
		return "back_end/logined/coupon/couponManagePage";
	}

	// 轉向商品券管理
	@GetMapping("/pro")
	public String redirectToProCpn() {
		return "redirect:/admin/procpn/list";
	}

	// 轉向活動券管理
	@GetMapping("/act")
	public String redirectToActCpn() {
		return "redirect:/admin/actcpn/list";
	}

	// 轉向商品券明細
	@GetMapping("/mempro")
	public String redirectToMemProCpn() {
		return "redirect:/admin/memprocpn/list";
	}

	// 轉向活動券明細
	@GetMapping("/memact")
	public String redirectToMemActCpn() {
		return "redirect:/admin/memactcpn/list";
	}

	// 轉向折價券操作日誌
	@GetMapping("/log")
	public String redirectToCouponLog() {
		return "redirect:/admin/log/list";
	}
}
