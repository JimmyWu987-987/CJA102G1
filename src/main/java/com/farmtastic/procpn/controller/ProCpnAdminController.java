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
@RequestMapping("/admin/procpn")
public class ProCpnAdminController {
	@Autowired
	private ProCpnService proCpnSvc;

	public String addProCpn(Model model) {
		ProCpnVO procpnVO = new ProCpnVO();
		model.addAttribute(procpnVO);
		return "/back_end/logined/procpn/addProCpn";
	}

	// 查詢全部折價卷
	@GetMapping("listAllProCpn")
	public String listAll(Model model) {
		List<ProCpnVO> list = proCpnSvc.getAll();
		model.addAttribute("coupons", list);
		return "/back_end/logined/procpn/listAllProCpn";
	}
}
