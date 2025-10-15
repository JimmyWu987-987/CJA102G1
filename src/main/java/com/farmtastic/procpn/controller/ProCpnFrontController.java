package com.farmtastic.procpn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.farmtastic.procpn.model.ProCpnService;
import com.farmtastic.procpn.model.ProCpnVO;

@Controller
@RequestMapping("/procpn")
public class ProCpnFrontController {
	@Autowired
	private ProCpnService proCpnSvc;

	// 顯示所有「啟用中」的折價券
	@GetMapping("/listActive")
	public String listActiveCoupons(Model model) {
		List<ProCpnVO> activeCoupons = proCpnSvc.getActiveProCpn();
		model.addAttribute("coupons", activeCoupons);
		return "/front_end/procpn/listActive"; // 顯示前台活動券頁
	}
}
