package com.farmtastic.memprocpn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.farmtastic.common.mapper.MemProCpnMapperImp;
import com.farmtastic.memprocpn.dto.MemProCpnFormDTO;
import com.farmtastic.memprocpn.model.MemProCpnServiceImp;
import com.farmtastic.memprocpn.model.MemProCpnVO;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/memprocpn")
public class MemProCpnAdminController {
	@Autowired
	private MemProCpnServiceImp memProCpnSvc;
	@Autowired
	private MemProCpnMapperImp mapper;

	// 顯示新增頁面
	@GetMapping("/addForm")
	public String showAddForm(Model model) {
		model.addAttribute("memProCpnForm", new MemProCpnFormDTO());
		return "/back_end/logined/memprocpn/addMemProCpn";
	}

	// 新增折價卷
	@PostMapping("/add")
	public String addProCpn(@Valid @ModelAttribute("memProCpnForm") MemProCpnFormDTO form, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			System.out.println("驗證錯誤數：" + result.getErrorCount());
			return "/back_end/logined/memprocpn/addMemProCpn";
		}
		MemProCpnVO vo = mapper.toEntity(form);
		memProCpnSvc.addMemProCpn(vo);
		return "redirect:/admin/procpn/listAllProCpn"; // 新增後回列表
	}

	@GetMapping("/listValidByMember")
	// 查「某會員」未使用且有效折價券
	public String listValidCpn(@RequestParam Integer memId, Model model) {
		model.addAttribute("coupons", memProCpnSvc.getValidCpnsByMember(memId));
		return "/back_end/logined/memprocpn/listValidByMember";
	}
}
