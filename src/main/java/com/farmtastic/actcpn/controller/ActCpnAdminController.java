package com.farmtastic.actcpn.controller;

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

import com.farmtastic.actcpn.dto.ActCpnFormDTO;
import com.farmtastic.actcpn.model.ActCpnService;
import com.farmtastic.actcpn.model.ActCpnVO;
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

	// 查詢全部折價卷
	@GetMapping("/list")
	public String listAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			Model model) {
		Page<ActCpnVO> pageData = actCpnSvc.findPagedActCpn(page, size);
		model.addAttribute("pageData", pageData);
		return "/back_end/logined/actcpn/listAllActCpn";
	}

	// 顯示新增頁面
	@GetMapping("/addForm")
	public String showAddForm(Model model) {
		model.addAttribute("mode", "add");
		model.addAttribute("actionUrl", "/admin/actcpn/add");
		model.addAttribute("actCpnForm", new ActCpnFormDTO());
		return "/back_end/logined/actcpn/actCpnForm";
	}

	// 新增折價卷
	@PostMapping("/add")
	public String addProCpn(@Valid @ModelAttribute("actCpnForm") ActCpnFormDTO form, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("mode", "add");
			model.addAttribute("actionUrl", "/admin/actcpn/add");
			return "/back_end/logined/actcpn/actCpnForm";
		}
		actCpnSvc.addActCpn(mapper.toEntity(form));
		redirectAttributes.addFlashAttribute("successMessage", "新增折價券成功！");
		return "redirect:/admin/actcpn/list";
	}

	// 刪除
	@GetMapping("/delete/{id}")
	public String deleteActCpn(@PathVariable Integer id) {
		actCpnSvc.deleteActCpn(id);
		return "redirect:/admin/actcpn/list";
	}
}
