package com.farmtastic.pro.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
import com.farmtastic.pro.model.LowRatePro;
import com.farmtastic.pro.model.Pro;
import com.farmtastic.pro.model.ProService;
import com.farmtastic.procate.model.Procate;
import com.farmtastic.procate.model.ProcateService;
import com.farmtastic.procom.model.ProComService;
import com.farmtastic.proimage.model.ProImageService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/pro")
public class ProController {

	private static final Logger logger = LoggerFactory.getLogger(ProController.class);

	@Autowired
	ProService proSvc;

	@Autowired
	ProcateService procateSvc;

	@Autowired
	ProImageService proimageSvc;
	
	@Autowired
	ProComService proComService;

	@GetMapping("/admin/add")
	public String adminAddPro(Model model) {
		model.addAttribute("action", "/pro/admin/insert");
		model.addAttribute("pageTitle", "管理員新增產品");
		model.addAttribute("pro", new Pro()); // 提供一個空的Pro物件給表單
		return "back_end/logined/admin/pro/proForm";
	}

	@PostMapping("/admin/insert")
	public String adminInsert(@Valid @ModelAttribute("pro") Pro pro, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("action", "/pro/admin/insert");
			model.addAttribute("pageTitle", "管理員新增產品");
			return "back_end/logined/admin/pro/proForm";
		}
		proSvc.addPro(pro);
		redirectAttributes.addFlashAttribute("success", "- (管理員新增成功)");
		return "redirect:/pro/listAllPro";
	}

	@GetMapping("/admin/update/{proId}")
	public String adminShowUpdateForm(@PathVariable("proId") Integer proId, Model model) {
		Pro pro = proSvc.getOnePro(proId);
		model.addAttribute("pro", pro);
		model.addAttribute("action", "/pro/admin/update");
		model.addAttribute("pageTitle", "管理員修改產品");
		return "back_end/logined/admin/pro/proForm";
	}
	
	@PostMapping("/admin/update")
	public String adminUpdate(@Valid @ModelAttribute("pro") Pro pro, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("action", "/pro/admin/update");
			model.addAttribute("pageTitle", "管理員修改產品");
			return "back_end/logined/admin/pro/proForm";
		}
		proSvc.updatePro(pro);
		redirectAttributes.addFlashAttribute("success", "- (管理員修改成功)");
		return "redirect:/pro/listAllPro";
	}
	
	@PostMapping("/admin/delete")
	public String adminDelete(@RequestParam("proId") Integer proId, RedirectAttributes redirectAttributes) {
		proimageSvc.deleteImageByProId(proId.longValue());
		proSvc.deletePro(proId);
		redirectAttributes.addFlashAttribute("success", "- (管理員刪除成功)");
		return "redirect:/pro/listAllPro";
	}
	
	
	   @GetMapping("/admin/low-rated")
	    public String showLowRatedProducts(Model model) {
	        List<LowRatePro> lowRatedProducts = proComService.findLowRatedProducts();
	        
	        List<LowRatePro> enrichedProducts = lowRatedProducts.stream().map(dto -> {
	            Pro product = proSvc.getOnePro(dto.getProId());
	            if (product != null && product.getFmemId() != null) {
	                dto.setFmemId(product.getFmemId().getFmemId());
	            }
	            return dto;
	        }).collect(Collectors.toList());

	        model.addAttribute("LowRatePro", enrichedProducts);
	        return "back_end/logined/admin/pro/low_rate";
	    }
		
		
	    // 處理商品下架的請求
	    @PostMapping("/admin/takedown")
	    public String takeDownProduct(@RequestParam("proId") Integer proId, RedirectAttributes redirectAttributes) {
	        try {
	            proSvc.takeDownProduct(proId);
	            redirectAttributes.addFlashAttribute("successMessage", "商品 #" + proId + " 已成功下架。");
	        } catch (Exception e) {
	            redirectAttributes.addFlashAttribute("errorMessage", "商品下架失敗，請稍後再試。");
	        }
	        return "redirect:/pro/admin/low-rated";
	    }
	
	// ================= 小農專用 (Fmem) =================

	@GetMapping("/fmem/add")
	public String fmemAddPro(HttpSession session, Model model) {
		// 【修正】從 session 獲取登入的小農會員資訊，key值參考 FmemController 改為 "loggedInFmember"
		Fmem loggedInFmem = (Fmem) session.getAttribute("loggedInFmember");
		if (loggedInFmem == null) {
			// 【修正】如果 session 中沒有小農資訊，則重導向到登入頁面
			return "redirect:/fmem/showFmemRegLoginForm";
		}
		Pro pro = new Pro();
		pro.setFmemId(loggedInFmem);

		model.addAttribute("pro", pro);
		model.addAttribute("action", "/pro/fmem/insert");
		model.addAttribute("pageTitle", "小農新增產品");
		return "front_end/farmer/logined/fmemProfile/fmemProForm";
	}

	@PostMapping("/fmem/insert")
	public String fmemInsert(@Valid @ModelAttribute("pro") Pro pro, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("action", "/pro/fmem/insert");
			model.addAttribute("pageTitle", "小農新增產品");
			return "front_end/farmer/logined/fmemProfile/fmemProForm";
		}
		proSvc.addPro(pro);
		redirectAttributes.addFlashAttribute("success", "- (新增成功)");
		return "redirect:/pro/fmem/myPro";
	}

	@GetMapping("/fmem/update/{proId}")
	public String fmemShowUpdateForm(@PathVariable("proId") Integer proId, Model model) {
		Pro pro = proSvc.getOnePro(proId);
		model.addAttribute("pro", pro);
		model.addAttribute("action", "/pro/fmem/update");
		model.addAttribute("pageTitle", "小農修改產品");
		return "front_end/farmer/logined/fmemProfile/fmemProForm";
	}

	@PostMapping("/fmem/update")
	public String fmemUpdate(@Valid @ModelAttribute("pro") Pro pro, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("action", "/pro/fmem/update");
			model.addAttribute("pageTitle", "小農修改產品");
			return "front_end/farmer/logined/fmemProfile/fmemProForm";
		}
		proSvc.updatePro(pro);
		redirectAttributes.addFlashAttribute("success", "- (修改成功)");
		return "redirect:/pro/fmem/myPro";
	}
	
	@PostMapping("/fmem/delete")
	public String fmemDelete(@RequestParam("proId") Integer proId, RedirectAttributes redirectAttributes) {
		proimageSvc.deleteImageByProId(proId.longValue());
		proSvc.deletePro(proId);
		redirectAttributes.addFlashAttribute("success", "- (刪除成功)");
		return "redirect:/pro/fmem/myPro";
	}
	
	@GetMapping("/fmem/myPro")
	public String fmemAllPro(HttpSession session, Model model) {
		
		logger.info("=== 進入 fmemAllPro 方法 ===");
		
		// 【修正】從 session 獲取登入的小農會員資訊，key值參考 FmemController 改為 "loggedInFmember"
		Fmem loggedInFmem = (Fmem) session.getAttribute("loggedInFmember");
		if (loggedInFmem == null) {
			
			logger.warn("Session中沒有登入的小農資訊");
			
			// 【修正】如果 session 中沒有小農資訊，則重導向到登入頁面
			return "redirect:/fmem/showFmemRegLoginForm";
		}
		Integer currentFmemId = loggedInFmem.getFmemId();
		
		logger.info("當前小農ID: {}", currentFmemId);

		List<Pro> proListData = proSvc.findByFmemId(currentFmemId);
		
		logger.info("查詢到的商品數量: {}", proListData.size());
		
		proListData.forEach(pro -> proimageSvc.findFirstImageByProId(pro.getProId().longValue()).ifPresent(pro::setProImage));
		
		model.addAttribute("proListData", proListData);
		// 【註解】fmemId 已包含在 session 的 loggedInFmember 物件中，前端可以直接從 session 抓取，此行可省略
		// model.addAttribute("fmemId", currentFmemId);
		return "front_end/farmer/logined/fmemProfile/fmemAllPro";
	}
	
	

	// ================= 共用及其他頁面 =================

	@GetMapping("listAllPro")
	public String listAllPro(HttpServletRequest req, Model model) {
		model.addAttribute("currentUri", req.getRequestURI());
		List<Pro> proListData = proSvc.getAll();
		proListData.forEach(pro -> proimageSvc.findFirstImageByProId(pro.getProId().longValue()).ifPresent(pro::setProImage));
		model.addAttribute("proListData", proListData);
		return "back_end/logined/admin/pro/listAllPro";
	}

	@PostMapping("listPros_ByCompositeQuery")
	public String listPros_ByCompositeQuery(HttpServletRequest req, Model model) {
		model.addAttribute("currentUri", req.getRequestURI());
		Map<String, String[]> map = req.getParameterMap();
		List<Pro> list = proSvc.getAll(map);
		list.forEach(pro -> proimageSvc.findFirstImageByProId(pro.getProId().longValue()).ifPresent(pro::setProImage));
		model.addAttribute("proListData", list);
		return "back_end/logined/admin/pro/listAllPro";
	}
	
	@GetMapping("/view/{proid}")
	public String showOneProductPage(@PathVariable("proid") Integer proId, Model model) {
		Pro pro = proSvc.getOnePro(proId);
		model.addAttribute("pro", pro);
		return "back_end/pro/onePro";
	}
	
	@ModelAttribute("procateListData")
	public List<Procate> populateProcateList() {
		return procateSvc.getAll();
	}
}