package com.farmtastic.procpn.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.farmtastic.common.enums.IsActive;
import com.farmtastic.common.mapper.ProCpnMapper;
import com.farmtastic.procpn.dto.ProCpnFormDTO;
import com.farmtastic.procpn.model.ProCpnService;
import com.farmtastic.procpn.model.ProCpnVO;

import jakarta.validation.Valid;

//商品折價卷規則
@Controller
@RequestMapping("/admin/procpn")
public class ProCpnAdminController {
	@Autowired
	private ProCpnService proCpnSvc;
	@Autowired
	private ProCpnMapper mapper;

	// ✅ 共用模板名稱
	private static final String VIEW_PATH = "back_end/logined/procpn/listAllProCpn";

	// 統一分頁設定
	private Pageable buildPageable(int page, int size) {
		return PageRequest.of(page, size, Sort.by("proCpnId").ascending());
	}

	// 查詢全部折價卷
	@GetMapping("/list")
	public String listAll(Model model) {
		List<ProCpnVO> coupons = proCpnSvc.getAll();
		model.addAttribute("coupons", coupons);
		return VIEW_PATH;
	}

//詳細頁面
	@GetMapping("/view/{id}")
	public String viewProCpn(@PathVariable Integer id, Model model) {
		Optional<ProCpnVO> optional = proCpnSvc.getById(id);
		if (optional.isEmpty()) {
			model.addAttribute("error", "查無折價券 ID：" + id);
			return "redirect:/admin/procpn/list";
		}
		model.addAttribute("coupon", optional.get());
		return "back_end/logined/procpn/viewProCpn";
	}

	// 顯示新增頁面
	@GetMapping("/addForm")
	public String showAddForm(Model model) {
		model.addAttribute("mode", "add");
		model.addAttribute("actionUrl", "/admin/procpn/add");
		model.addAttribute("proCpnForm", new ProCpnFormDTO());
		return "back_end/logined/procpn/proCpnForm";
	}

	// 新增折價卷
	@PostMapping("/add")
	public String addProCpn(@Valid @ModelAttribute("proCpnForm") ProCpnFormDTO form, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("mode", "add");
			model.addAttribute("actionUrl", "/admin/procpn/add");
			return "back_end/logined/procpn/proCpnForm";
		}
		proCpnSvc.addProCpn(mapper.toEntity(form));
		redirectAttributes.addFlashAttribute("successMessage", "新增折價券成功！");
		return "redirect:/admin/procpn/list";
	}

	@GetMapping("/find")
	public String findOneProCpn(@RequestParam(required = false) Integer id, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, Model model) {
		// 1. 判斷空值
		if (id == null) {
			model.addAttribute("error", "請輸入折價券編號！");
			model.addAttribute("pageData", Page.empty()); // 避免 pageData=null
			return VIEW_PATH;

		}

		// 2. 查資料
		Optional<ProCpnVO> optional = proCpnSvc.getById(id);

		// 3.處理結果
		Page<ProCpnVO> pageData = optional.map(cpn -> new PageImpl<>(List.of(cpn), buildPageable(page, size), 1))
				.orElseGet(() -> {
					model.addAttribute("error", "查無此折價券編號：" + id);
					return new PageImpl<>(List.of(), buildPageable(page, size), 0);
				});
		model.addAttribute("pageData", pageData);
		return VIEW_PATH;
	}

	/** 模糊搜尋折價券名稱（後台） */
	@GetMapping("/search")
	public String searchCpns(@RequestParam("keyword") String keyword, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, Model model) {

		List<ProCpnVO> resultList = proCpnSvc.findByKeyword(keyword);
		model.addAttribute("coupons", resultList);
		model.addAttribute("keyword", keyword);
		return VIEW_PATH;
	}

	/** 日期篩選 */
	@GetMapping("/filter")
	public String filterCpns(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, Model model) {
		Page<ProCpnVO> pageData = proCpnSvc.filterByDateRange(start, end, buildPageable(page, size));
		model.addAttribute("start", start);
		model.addAttribute("end", end);
		model.addAttribute("pageData", pageData);
		return VIEW_PATH;
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

//改變狀態
	@GetMapping("/toggleStatus/{id}/{status}")
	public String deleteProCpn(@PathVariable Integer id, @PathVariable String status) {
		IsActive newStatus = "ACTIVE".equalsIgnoreCase(status) ? IsActive.ACTIVE : IsActive.INACTIVE;
		proCpnSvc.changeProCpnStatus(id, newStatus);
		return "redirect:/admin/procpn/list";
	}

}
