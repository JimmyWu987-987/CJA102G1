package com.farmtastic.procpn.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.common.mapper.ProCpnMapper;
import com.farmtastic.procpn.dto.DateRangeRequestDTO;
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

//詳細頁面
	@GetMapping("/view/{id}")
	public String viewProCpn(@PathVariable Integer id, Model model) {
		Optional<ProCpnVO> optional = proCpnSvc.getById(id);
		if (optional.isEmpty()) {
			model.addAttribute("error", "查無折價券 ID：" + id);
			return "redirect:/admin/procpn/list";
		}
		model.addAttribute("coupon", optional.get());
		return "/back_end/logined/procpn/viewProCpn";
	}

	// 顯示新增頁面
	@GetMapping("/addForm")
	public String showAddForm(Model model) {
		model.addAttribute("mode", "add");
		model.addAttribute("actionUrl", "/admin/procpn/add");
		model.addAttribute("proCpnForm", new ProCpnFormDTO());
		return "/back_end/logined/procpn/proCpnForm";
	}

	// 新增折價卷
	@PostMapping("/add")
	public String addProCpn(@Valid @ModelAttribute("proCpnForm") ProCpnFormDTO form, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("mode", "add");
			model.addAttribute("actionUrl", "/admin/procpn/add");
			return "/back_end/logined/procpn/proCpnForm";
		}
		proCpnSvc.addProCpn(mapper.toEntity(form));
		redirectAttributes.addFlashAttribute("successMessage", "新增折價券成功！");
		return "redirect:/admin/procpn/list";
	}

	// 查詢全部折價卷
	@GetMapping("/list")
	public String listAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			Model model) {
		Page<ProCpnVO> pageData = proCpnSvc.findPagedProCpn(page, size);
		model.addAttribute("pageData", pageData);
		return "/back_end/logined/procpn/listAllProCpn";
	}

	@GetMapping("/find")
	public String findOneProCpn(@RequestParam(required = false) Integer id, Model model) {
		// 1. 判斷空值
		if (id == null) {
			model.addAttribute("error", "請輸入折價券編號！");
			return "/back_end/logined/procpn/listAllProCpn"; // 返回查詢頁
		}

		// 2. 查資料
		Optional<ProCpnVO> optional = proCpnSvc.getById(id);

		// 3.處理結果
		if (optional.isPresent()) {
			model.addAttribute("coupons", List.of(optional.get()));
		} else {
			model.addAttribute("error", "查無此折價券編號：" + id);
			model.addAttribute("coupons", List.of());
		}

		return "/back_end/logined/procpn/listAllProCpn";
	}

	/** 模糊搜尋折價券名稱（後台） */
	@GetMapping("/search")
	public String searchCpns(@RequestParam("keyword") String keyword, Model model) {
		List<ProCpnVO> cpn = proCpnSvc.searchProCpnByName(keyword);
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
		List<ProCpnVO> coupons = proCpnSvc.findProCpnByDateRange(request.getStart(), request.getEnd());

		model.addAttribute("coupons", coupons);
		model.addAttribute("start", request.getStart());
		model.addAttribute("end", request.getEnd());
		return "/back_end/logined/procpn/listAllProCpn";
	}

	@GetMapping("/edit/{id}")
	public String editProCpn(@PathVariable Integer id, Model model) {
		Optional<ProCpnVO> optional = proCpnSvc.getById(id);
		if (optional.isEmpty()) {
			model.addAttribute("error", "查無折價券 ID：" + id);
			return "redirect:/admin/procpn/list";
		}
		model.addAttribute("mode", "edit");
		model.addAttribute("actionUrl", "/admin/procpn/update");
		model.addAttribute("proCpnForm", mapper.toFormDTO(optional.get()));
		return "/back_end/logined/procpn/proCpnForm";
	}

//更新
	@PostMapping("/update")
	public String updateProCpn(@Valid @ModelAttribute("proCpnForm") ProCpnFormDTO form, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			model.addAttribute("mode", "edit");
			model.addAttribute("actionUrl", "/admin/procpn/update");
			return "/back_end/logined/procpn/proCpnForm";
		}

		ProCpnVO vo = mapper.toEntity(form);
		proCpnSvc.updateProCpn(vo);
		return "redirect:/admin/procpn/list";
	}

//刪除
	@GetMapping("/delete/{id}")
	public String deleteProCpn(@PathVariable Integer id) {
		proCpnSvc.deleteProCpn(id);
		return "redirect:/admin/procpn/list";
	}

//	// 分頁
//	@GetMapping("/listPaged")
//	public String listPaged(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
//			Model model) {
//		Page<ProCpnVO> pageData = proCpnSvc.findPagedProCpn(page, size);
//		model.addAttribute("pageData", pageData);
//		return "/back_end/logined/procpn/listAllProCpn";
//	}
}
