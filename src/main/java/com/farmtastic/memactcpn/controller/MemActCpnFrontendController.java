package com.farmtastic.memactcpn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.farmtastic.memactcpn.model.MemActCpnServiceImp;
import com.farmtastic.memactcpn.model.MemActCpnVO;
import com.farmtastic.member.model.Mem;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mem/memactcpn")
public class MemActCpnFrontendController {
	@Autowired
	private MemActCpnServiceImp memActCpnSvc;

	/**
	 * 會員查看自己的折價券列表
	 */
	@GetMapping("/list")
	public String viewMyCoupons(HttpSession session, Model model) {
		Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");
		Integer memId = loggedInMember.getMemId();

		// 查詢該會員所有折價券
		List<MemActCpnVO> coupons = memActCpnSvc.getCpnsByMember(memId);
		// System.out.print("Coupons: " + coupons);

		model.addAttribute("member", loggedInMember);
		model.addAttribute("coupons", coupons);
		return "front_end/customer/logined/memactcpn/memActCpnList"; // 對應 Thymeleaf 頁面
	}
}
