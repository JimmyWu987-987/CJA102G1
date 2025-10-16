package com.farmtastic.memprocpn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.farmtastic.memactcpn.model.MemActCpnServiceImp;
import com.farmtastic.memactcpn.model.MemActCpnVO;
import com.farmtastic.member.model.Mem;
import com.farmtastic.memprocpn.model.MemProCpnServiceImp;
import com.farmtastic.memprocpn.model.MemProCpnVO;

import jakarta.servlet.http.HttpSession;

//折價券（活動券 + 商品券）
@Controller
@RequestMapping("/mem/memcpn")
public class MemProCpnFrontendController {
	@Autowired
	private MemProCpnServiceImp memProCpnSvc;
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
		List<MemProCpnVO> proCoupons = memProCpnSvc.getCpnsByMember(memId);
		List<MemActCpnVO> actCoupons = memActCpnSvc.getCpnsByMember(memId);
		// System.out.print("Coupons: " + coupons);

		model.addAttribute("member", loggedInMember);
		// 傳給 Thymeleaf
		model.addAttribute("proCoupons", proCoupons);
		model.addAttribute("actCoupons", actCoupons);

		System.out.println("🟢 Logged in member: " + loggedInMember);
		System.out.println("🟢 proCoupons = " + proCoupons.size());
		System.out.println("🟢 actCoupons = " + actCoupons.size());
		return "front_end/customer/logined/memcpn/listAllCoupons"; // 對應 Thymeleaf 頁面
	}
}
