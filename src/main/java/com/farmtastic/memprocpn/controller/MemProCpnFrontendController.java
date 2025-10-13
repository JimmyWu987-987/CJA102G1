package com.farmtastic.memprocpn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.farmtastic.member.model.Mem;
import com.farmtastic.memprocpn.model.MemProCpnServiceImp;
import com.farmtastic.memprocpn.model.MemProCpnVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mem/memprocpn")
public class MemProCpnFrontendController {
	@Autowired
	private MemProCpnServiceImp memProCpnSvc;

	/**
	 * 會員查看自己的折價券列表
	 */
	@GetMapping("/list")
	public String viewMyCoupons(HttpSession session, Model model) {
		Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");
		Integer memId = loggedInMember.getMemId();

		// 查詢該會員所有折價券
		List<MemProCpnVO> coupons = memProCpnSvc.getCpnsByMember(memId);

		model.addAttribute("member", loggedInMember);
		model.addAttribute("coupons", coupons);
		return "front_end/customer/logined/memprocpn/memProCpnList"; // 對應 Thymeleaf 頁面
	}
}
