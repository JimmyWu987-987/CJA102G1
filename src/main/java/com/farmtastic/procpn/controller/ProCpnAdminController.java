package com.farmtastic.procpn.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.farmtastic.common.mapper.ProCpnMapper;
import com.farmtastic.procpn.dto.DateRangeRequestDTO;
import com.farmtastic.procpn.dto.ProCpnAdminDTO;
import com.farmtastic.procpn.dto.ProCpnFormDTO;
import com.farmtastic.procpn.model.ProCpnService;
import com.farmtastic.procpn.model.ProCpnVO;

import jakarta.validation.Valid;

//折價卷規則
@Controller
@RequestMapping("/admin/procpn")
public class ProCpnAdminController {
	@Autowired
	private ProCpnService proCpnSvc;
	@Autowired
	private ProCpnMapper mapper;

	// 顯示新增頁面
	@GetMapping("/addForm")
	public String showAddForm(Model model) {
		model.addAttribute("proCpnForm", new ProCpnFormDTO());
		return "/back_end/logined/procpn/addProCpn";
	}

	// 新增折價卷
	@PostMapping("/add")
	public String addProCpn(@Valid @ModelAttribute("proCpnForm") ProCpnFormDTO form, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			System.out.println("驗證錯誤數：" + result.getErrorCount());
			return "/back_end/logined/procpn/addProCpn";
		}
		ProCpnVO vo = mapper.toEntity(form);
		proCpnSvc.addProCpn(vo);
		return "redirect:/admin/procpn/listAllProCpn"; // 新增後回列表
	}

	// 查詢全部折價卷
	@GetMapping("/listAllProCpn")
	public String listAll(Model model) {
		// List<ProCpnVO> list = proCpnSvc.findAllCoupons();
		model.addAttribute("coupons", proCpnSvc.findAllProCpn());
		return "/back_end/logined/procpn/listAllProCpn";
	}

	// 查單一折價券
	@GetMapping("/find")
	public String findOneProCpn(@RequestParam("id") Integer id, Model model) {
		Optional<ProCpnAdminDTO> optional = proCpnSvc.getById(id);
		;
		if (optional.isPresent()) {
			model.addAttribute("coupons", List.of(optional.get())); // 用 List 包起來讓 Thymeleaf 循環能顯示一筆
		} else {
			model.addAttribute("error", "查無此折價券編號：" + id);
			model.addAttribute("coupons", List.of());
		}
		return "/back_end/logined/procpn/listAllProCpn";
	}

	/** 模糊搜尋折價券名稱（後台） */
	@GetMapping("/search")
	public String searchCpns(@RequestParam("keyword") String keyword, Model model) {
		List<ProCpnAdminDTO> cpn = proCpnSvc.searchProCpnByName(keyword);
		model.addAttribute("coupons", cpn);
		model.addAttribute("keyword", keyword);
		return "/back_end/logined/procpn/listAllProCpn";
	}

	// 查詢指定日期範圍內的折價券
	@GetMapping("/filter")
	public String searchCpnByDate(@Valid DateRangeRequestDTO request, BindingResult result, Model model) {
		// 驗證失敗 → 回到畫面 + 顯示紅字
		if (result.hasErrors()) {
			model.addAttribute("errors", result.getAllErrors());
			model.addAttribute("coupons", proCpnSvc.findAllProCpn()); // 顯示全部
			return "/back_end/logined/procpn/listAllProCpn";
		}

		// 驗證成功 → 查詢區間資料
		List<ProCpnAdminDTO> coupons = proCpnSvc.findProCpnByDateRange(request.getStart(), request.getEnd());

		model.addAttribute("coupons", coupons);
		model.addAttribute("start", request.getStart());
		model.addAttribute("end", request.getEnd());
		return "/back_end/logined/procpn/listAllProCpn";
	}

}
