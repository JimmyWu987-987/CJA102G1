package com.farmtastic.procpn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.farmtastic.procpn.model.ProCpnService;
import com.farmtastic.procpn.model.ProCpnVO;

//折價卷規則
@Controller
@RequestMapping("/admin/procpn")
public class ProCpnAdminController {
	@Autowired
	private ProCpnService proCpnSvc;

	// 顯示新增頁面
	@GetMapping("/addForm")
	public String showAddForm(Model model) {
		model.addAttribute("proCpnVO", new ProCpnVO());
		return "/back_end/logined/procpn/addProCpn";
	}

	// 新增折價卷
	@PostMapping("/add")
	public String addProCpn(@ModelAttribute ProCpnVO procpnVO) {
		proCpnSvc.addProCpn(procpnVO);
		return "redirect:/admin/procpn/listAllProCpn"; // 新增後回列表
	}

	// 查詢全部折價卷
	@GetMapping("listAllProCpn")
	public String listAll(Model model) {
		List<ProCpnVO> list = proCpnSvc.getAll();
		model.addAttribute("coupons", list);
		return "/back_end/logined/procpn/listAllProCpn";
	}
}
