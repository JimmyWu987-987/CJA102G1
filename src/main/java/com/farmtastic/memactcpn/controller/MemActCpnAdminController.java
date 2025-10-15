package com.farmtastic.memactcpn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.farmtastic.common.mapper.MemActCpnMapperImp;
import com.farmtastic.memactcpn.model.MemActCpnServiceImp;
import com.farmtastic.memactcpn.model.MemActCpnVO;

@Controller
@RequestMapping("/admin/memactcpn")
public class MemActCpnAdminController {

	@Autowired
	private MemActCpnServiceImp memActCpnSvc;
	@Autowired
	private MemActCpnMapperImp mapper;

//	// 顯示新增頁面
//	@GetMapping("/addForm")
//	public String showAddForm(Model model) {
//		model.addAttribute("memActCpnForm", new MemActCpnFormDTO());
//		return "/back_end/logined/memactcpn/addActProCpn";
//	}
//
//	// 新增折價卷
//	@PostMapping("/add")
//	public String addActCpn(@Valid @ModelAttribute("memActCpnForm") MemActCpnFormDTO form, BindingResult result,
//			Model model) {
//		if (result.hasErrors()) {
//			System.out.println("驗證錯誤數：" + result.getErrorCount());
//			return "/back_end/logined/memactcpn/addMemActCpn";
//		}
//		MemActCpnVO vo = mapper.toEntity(form);
//		memActCpnSvc.addMemActCpn(vo);
//		return "redirect:/admin/actcpn/listAllActCpn"; // 新增後回列表
//	}

	// 列出全部會員折價券
	@GetMapping("/list")
	public String listAll(Model model) {
		List<MemActCpnVO> memCoupons = memActCpnSvc.getAll();
		model.addAttribute("memCoupons", memCoupons);
		return "back_end/logined/memactcpn/listAllMemActCpn";
	}

	@GetMapping("/listValidByMember")
	// 查「某會員」未使用且有效折價券
	public String listValidCpn(@RequestParam Integer memId, Model model) {
		model.addAttribute("coupons", memActCpnSvc.getValidCpnsByMember(memId));
		return "back_end/logined/memprocpn/listValidByMember";
	}

	@GetMapping("/view/{cpnHolderDetailId}")
	public String viewMemActCpn(@PathVariable Integer cpnHolderDetailId, Model model) {
		MemActCpnVO memActCpn = memActCpnSvc.getOne(cpnHolderDetailId);
		if (memActCpn == null) {
			model.addAttribute("error", "查無此筆資料");
			return "redirect:/admin/memactcpn/list";
		}

		model.addAttribute("memActCpn", memActCpn);
		return "back_end/logined/memactcpn/viewMemActCpn"; // 對應查看詳情頁
	}
}
