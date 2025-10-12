package com.farmtastic.pro.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.pro.model.Pro;
import com.farmtastic.pro.model.ProService;
import com.farmtastic.procate.model.Procate;
import com.farmtastic.procate.model.ProcateService;
import com.farmtastic.proimage.model.ProImageService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
@RequestMapping("/pro")
public class ProController {

//	private static final Logger logger = LoggerFactory.getLogger(ProController.class);

	@Autowired
	ProService proSvc;

	@Autowired
	ProcateService procateSvc;

	@Autowired
	ProImageService proimageSvc;

	/**
	 * 處理新增產品頁面的GET請求
	 */
	@GetMapping("addPro")
	public String addPro(ModelMap model) {
		return "back_end/pro/addPro";
	}

	@PostMapping("insert")
	public String insert(@Valid @ModelAttribute("pro") Pro pro, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "back_end/pro/addPro";
		}
		
		proSvc.addPro(pro);
		
		redirectAttributes.addFlashAttribute("success", "- (新增成功)");
		return "redirect:/pro/listAllPro"; 
	}

	/**
	 * 處理進入修改頁面的請求
	 */
	@GetMapping("update/{proId}")
	public String showUpdateForm(@PathVariable("proId") Integer proId, Model model) {
		Pro pro = proSvc.getOnePro(proId);
		
		if (pro.getProcateId() == null) {
			pro.setProcateId(new Procate());
		}
		
	    if (pro.getFmemId() == null) {
	        pro.setFmemId(new Fmem());
	    }

		model.addAttribute("pro", pro);
		return "back_end/pro/update_pro_input";
	}

	@PostMapping("update")
	public String update(@Valid @ModelAttribute("pro") Pro pro, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "back_end/pro/update_pro_input";
		}

		proSvc.updatePro(pro);

		redirectAttributes.addFlashAttribute("success", "- (修改成功)");
		return "redirect:/pro/listAllPro";
	}

	@PostMapping("/delete")
	public String delete(@RequestParam("proId") Integer proId, 
	                     RedirectAttributes redirectAttributes) {
	    proimageSvc.deleteImageByProId(proId.longValue());
	    proSvc.deletePro(proId);
	    redirectAttributes.addFlashAttribute("success", "- (刪除成功)");
	    return "redirect:/pro/listAllPro";
	}
	
	private String listByCriteria(Map<String, String[]> map, HttpServletRequest req, Model model) {
		model.addAttribute("currentUri", req.getRequestURI());
		List<Pro> proListData = proSvc.getAll(map);

		proListData.forEach(pro -> {
			proimageSvc.findFirstImageByProId(pro.getProId().longValue())
			           .ifPresent(pro::setProImage);
		});

		model.addAttribute("proListData", proListData);
		return "back_end/pro/listAllPro";
	}

	@GetMapping("listAllPro")
	public String listAllPro(HttpServletRequest req, Model model) {
		// ★★★ 呼叫重構後的方法，傳入一個空的 map 來查詢所有資料 ★★★
		return listByCriteria(new HashMap<>(), req, model);
	}

	@PostMapping("listPros_ByCompositeQuery")
	public String listPros_ByCompositeQuery(HttpServletRequest req, Model model) {
		// ★★★ 呼叫重構後的方法，傳入從請求中獲取的 map 來進行複合查詢 ★★★
		return listByCriteria(req.getParameterMap(), req, model);
	}
	

	@ModelAttribute("procateListData")
	public List<Procate> populateProcateList() {
		return procateSvc.getAll();
	}

	@ModelAttribute("pro")
	public Pro initializePro() {
		return new Pro();
	}

	@GetMapping("/view/{proid}")
	public String showOneProductPage(@PathVariable("proid") Integer proId, Model model) {
		Pro pro = proSvc.getOnePro(proId);
		model.addAttribute("pro", pro);
		return "back_end/pro/onePro";
	}
}