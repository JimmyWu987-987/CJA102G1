package com.farmtastic.procpn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.farmtastic.procpn.dto.ProCpnFormDTO;
import com.farmtastic.procpn.model.ProCpnService;

//折價卷規則
@Controller
@RequestMapping("/admin/procpn")
public class ProCpnAdminController {
	@Autowired
	private ProCpnService proCpnSvc;

	// 顯示新增頁面
	@GetMapping("/addForm")
	public String showAddForm(Model model) {
		model.addAttribute("proCpnVO", new ProCpnFormDTO());
		return "/back_end/logined/procpn/addProCpn";
	}

	// 新增折價卷
//	@PostMapping("/add")
//	public String addProCpn(@ModelAttribute ProCpnFormDTO form) {
//		proCpnSvc.addProCpn(form);
//		return "redirect:/admin/procpn/listAllProCpn"; // 新增後回列表
//	}

	// 查詢全部折價卷
	@GetMapping("/listAllProCpn")
	public String listAll(Model model) {
		// List<ProCpnVO> list = proCpnSvc.findAllCoupons();
		model.addAttribute("coupons", proCpnSvc.findAllProCpn());
		return "/back_end/logined/procpn/listAllProCpn";
	}
}
