package com.farmtastic.memprocpn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.farmtastic.common.mapper.MemProCpnMapperImp;
import com.farmtastic.memprocpn.model.MemProCpnServiceImp;
import com.farmtastic.memprocpn.model.MemProCpnVO;

@Controller
@RequestMapping("/admin/memprocpn")
public class MemProCpnAdminController {
	@Autowired
	private MemProCpnServiceImp memProCpnSvc;
	@Autowired
	private MemProCpnMapperImp mapper;

//	@Autowired
//	private SpinSyncScheduler spinSyncScheduler;

//	@GetMapping("/syncNow")
//	@ResponseBody
//	public String triggerSyncNow() {
//		spinSyncScheduler.syncPendingCoupons();
//		return "已手動同步 Redis 暫存折價券至資料庫！";
//	}

	// 列出全部會員折價券
	@GetMapping("/list")
	public String listAll(Model model) {
		List<MemProCpnVO> memCoupons = memProCpnSvc.getAll();
		model.addAttribute("memCoupons", memCoupons);
		return "/back_end/logined/memprocpn/listAllMemProCpn";
	}
//	// 顯示新增頁面
//	@GetMapping("/addForm")
//	public String showAddForm(Model model) {
//		model.addAttribute("memProCpnForm", new MemProCpnFormDTO());
//		return "/back_end/logined/memprocpn/addMemProCpn";
//	}
//
//	// 新增折價卷
//	@PostMapping("/add")
//	public String addProCpn(@Valid @ModelAttribute("memProCpnForm") MemProCpnFormDTO form, BindingResult result,
//			Model model) {
//		if (result.hasErrors()) {
//			System.out.println("驗證錯誤數：" + result.getErrorCount());
//			return "/back_end/logined/memprocpn/addMemProCpn";
//		}
//		MemProCpnVO vo = mapper.toEntity(form);
//		memProCpnSvc.addMemProCpn(vo);
//		return "redirect:/admin/procpn/listAllProCpn"; // 新增後回列表
//	}

	@GetMapping("/listValidByMember")
	// 查「某會員」未使用且有效折價券
	public String listValidCpn(@RequestParam Integer memId, Model model) {
		model.addAttribute("coupons", memProCpnSvc.getValidCpnsByMember(memId));
		return "/back_end/logined/memprocpn/listValidByMember";
	}

	@GetMapping("/view/{cpnHolderDetailId}")
	public String viewMemProCpn(@PathVariable Integer cpnHolderDetailId, Model model) {
		MemProCpnVO memProCpn = memProCpnSvc.getOne(cpnHolderDetailId);
		if (memProCpn == null) {
			model.addAttribute("error", "查無此筆資料");
			return "redirect:/admin/memprocpn/list";
		}

		model.addAttribute("memProCpn", memProCpn);
		return "/back_end/logined/memprocpn/viewMemProCpn"; // 對應查看詳情頁
	}

//	@GetMapping("/delete/{cpnHolderDetailId}")
//	public String deleteMemProCpn(@PathVariable Integer cpnHolderDetailId) {
//		memProCpnSvc.delete(cpnHolderDetailId);
//		return "redirect:/admin/memprocpn/list";
//	}
}
