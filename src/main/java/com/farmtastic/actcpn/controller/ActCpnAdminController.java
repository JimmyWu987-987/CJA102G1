package com.farmtastic.actcpn.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.farmtastic.actcpn.dto.ActCpnFormDTO;
import com.farmtastic.actcpn.model.ActCpnService;
import com.farmtastic.actcpn.model.ActCpnVO;
import com.farmtastic.common.enums.IsActive;
import com.farmtastic.common.mapper.ActCpnMapperImp;

import jakarta.validation.Valid;

//活動折價卷規則
@Controller
@RequestMapping("/admin/actcpn")
public class ActCpnAdminController {
	@Autowired
	private ActCpnService actCpnSvc;
	@Autowired
	private ActCpnMapperImp mapper;
	// 共用模板名稱
	private static final String VIEW_PATH = "back_end/logined/actcpn/listAllActCpn";
	private static final String ADD_PATH = "/back_end/logined/actcpn/actCpnForm";

	// 查詢全部折價卷
	@GetMapping("/list")
	public String listAll(Model model) {
		List<ActCpnVO> coupons = actCpnSvc.getAll();
		model.addAttribute("coupons", coupons);
		return VIEW_PATH;
	}

	// 詳細頁面
	@GetMapping("/view/{id}")
	public String viewProCpn(@PathVariable Integer id, Model model) {
		Optional<ActCpnVO> optional = actCpnSvc.getById(id);
		if (optional.isEmpty()) {
			model.addAttribute("error", "查無折價券 ID：" + id);
			return "redirect:/admin/actcpn/list";
		}
		model.addAttribute("coupon", optional.get());
		return "/back_end/logined/actcpn/viewActCpn";
	}

	// 顯示新增頁面
	@GetMapping("/addForm")
	public String showAddForm(Model model) {
		model.addAttribute("mode", "add");
		model.addAttribute("actionUrl", "/admin/actcpn/add");
		model.addAttribute("actCpnForm", new ActCpnFormDTO());
		return ADD_PATH;
	}

	// 新增折價卷
	@PostMapping("/add")
	public String addProCpn(@Valid @ModelAttribute("actCpnForm") ActCpnFormDTO form, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("mode", "add");
			model.addAttribute("actionUrl", "/admin/actcpn/add");
			return ADD_PATH;
		}
		actCpnSvc.addActCpn(mapper.toEntity(form));
		redirectAttributes.addFlashAttribute("successMessage", "新增折價券成功！");
		return "redirect:/admin/actcpn/list";
	}

	/** 模糊搜尋折價券名稱（後台） */
	@GetMapping("/search")
	public String searchCpns(@RequestParam("keyword") String keyword, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, Model model) {

		List<ActCpnVO> resultList = actCpnSvc.findByKeyword(keyword);
		model.addAttribute("coupons", resultList);
		model.addAttribute("keyword", keyword);
		return VIEW_PATH;
	}

	@GetMapping("/find")
	public String findOneProCpn(@RequestParam(required = false) Integer id, Model model) {
		List<ActCpnVO> coupons;
		// 1. 檢查是否有輸入 ID
		if (id == null) {
			coupons = actCpnSvc.getAll();
			model.addAttribute("coupons", coupons);
			return VIEW_PATH;

		}

		// 2. 查資料
		Optional<ActCpnVO> optional = actCpnSvc.getById(id);

		// 3.處理結果
		if (optional.isPresent()) {
			model.addAttribute("coupons", List.of(optional.get()));
		} else {
			model.addAttribute("error", "查無此折價券編號：" + id);
			coupons = actCpnSvc.getAll();
		}
		return VIEW_PATH;
	}

	@GetMapping("/edit/{id}")
	public String editProCpn(@PathVariable Integer id, Model model) {
		Optional<ActCpnVO> optional = actCpnSvc.getById(id);
		if (optional.isEmpty()) {
			model.addAttribute("error", "查無折價券 ID：" + id);
			return "redirect:/admin/procpn/list";
		}
		model.addAttribute("mode", "edit");
		model.addAttribute("actionUrl", "/admin/acrcpn/update");
		model.addAttribute("proCpnForm", mapper.toDTO(optional.get()));
		return ADD_PATH;
	}

//更新
	@PostMapping("/update")
	public String updateActCpn(@Valid @ModelAttribute("actCpnForm") ActCpnFormDTO form, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			model.addAttribute("mode", "edit");
			model.addAttribute("actionUrl", "/admin/actcpn/update");
		}

		ActCpnVO vo = mapper.toEntity(form);
		actCpnSvc.updateActCpn(vo);
		return "redirect:/admin/actcpn/list";
	}

//改變狀態
	@GetMapping("/toggleStatus/{id}/{status}")
	public String toggleActCpnStatus(@PathVariable Integer id, @PathVariable String status) {
		IsActive newStatus = "ACTIVE".equalsIgnoreCase(status) ? IsActive.ACTIVE : IsActive.INACTIVE;
		actCpnSvc.changeActCpnStatus(id, newStatus);
		return "redirect:/admin/actcpn/list";
	}

	/** 日期篩選 */
	@GetMapping("/filter")
	public String filterCpns(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end, Model model) {
		// 1.呼叫日期SERVICE
		List<ActCpnVO> filteredList = actCpnSvc.filterByDateRange(start, end);
		// 2.日期
		if (start == null && end == null) {
			// 沒選日期：顯示所有資料，但提示錯誤訊息
			filteredList = actCpnSvc.getAll();
			model.addAttribute("error", " 請選擇日期區間！已顯示全部資料");
		} else {
			// 有選日期就正常查詢
			filteredList = actCpnSvc.filterByDateRange(start, end);
			model.addAttribute("successMessage", "篩選成功，共 " + filteredList.size() + " 筆資料");
		}

		// 3.放進 model，讓 Thymeleaf 渲染
		model.addAttribute("coupons", filteredList);
		model.addAttribute("start", start);
		model.addAttribute("end", end);
		return VIEW_PATH;
	}
}
