package com.farmtastic.admin.controller;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.fmember.model.FmemService;
import com.farmtastic.member.model.Mem;
import com.farmtastic.member.model.MemService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class adminMemController {
	
	@Autowired
	MemService memSvc = new MemService();
	
	@Autowired
	FmemService fmemSvc = new FmemService();
	
//	---------------------一般會員----------------------
	@GetMapping("/listAllMems")
	public String listAllMems(Model model) {
		List<Mem> listMem = memSvc.getAll();
		model.addAttribute("listMem", listMem);
		return "/back_end/logined/mem/listAllMems";
	}
	
	@PostMapping("/updateMemAccStatus")
	public String updateMemAccStatus(
			Model model, 
			@RequestParam("memId") Integer memId,
			@RequestParam("accStatus") Byte accStatus,
			RedirectAttributes redirectAttrs) {
		
		memSvc.updateAccStatus(memId, accStatus);
		
		List<Mem> listMem = memSvc.getAll();
		model.addAttribute("listMem", listMem);
		
		redirectAttrs.addFlashAttribute("lastEditMemId", memId);
		return "redirect:/admin/listAllMems";
	}
	
	
//	---------------------小農會員----------------------
	@GetMapping("/listAllFmems")
	public String listAllFmems(Model model, HttpSession session) {
		List<Fmem> listFmem = fmemSvc.getAll();
		model.addAttribute("listFmem", listFmem);
//		session.setAttribute("listFmem", listFmem);
		return "/back_end/logined/fmem/listAllFmems";
	}
	
//	停權 / 復權
	@PostMapping("/updateFmemAccStatus")
	public String updateFmemAccStatus(
			Model model, 
			@RequestParam("fmemId") Integer fmemId,
			@RequestParam("accStatus") Byte accStatus,
			RedirectAttributes redirectAttrs) {
		
		fmemSvc.updateAccStatus(fmemId, accStatus);
		
		List<Fmem> listFmem = fmemSvc.getAll();
		model.addAttribute("listFmem", listFmem);
		
		redirectAttrs.addFlashAttribute("lastEditFmemId", fmemId);
		return "redirect:/admin/listAllFmems";
	}
	
	
//  前往審核頁面(總覽) -------------
	@GetMapping("/reviewFmems")
	public String reviewFmems(Model model) {
		List<Fmem> listFmem = fmemSvc.getAll();
		model.addAttribute("listFmem", listFmem);
		return "/back_end/logined/fmem/reviewFmems";
	}
	
	
//  前往審核頁面(單一小農)
	@PostMapping("/reviewFmem")
	public String reviewFmem(
			Model model, 
			@RequestParam("fmemId") String fmemId,
			HttpSession session) {
//		List<Fmem> listFmem = fmemSvc.getAll();
		model.addAttribute("fmemId", fmemId);
		session.setAttribute("fmemId", fmemId);

		Fmem fmem = fmemSvc.getOneByFmemId(Integer.valueOf(fmemId));
		
		if(fmem.getInsurPic() != null) {
			String insurPicBase64 = Base64.getEncoder().encodeToString(fmem.getInsurPic());
			model.addAttribute("insurPicBase64", insurPicBase64);
		}
		if(fmem.getLandPic() != null) {
			String landPicBase64 = Base64.getEncoder().encodeToString(fmem.getLandPic());
			model.addAttribute("landPicBase64", landPicBase64);
		}
		
		
		model.addAttribute("fmem", fmem);
		session.setAttribute("fmem", fmem);
		return "/back_end/logined/fmem/reviewFmem";
	}
	
	
	@PostMapping("/decideAccReview")
	public String decideAccReview(
			Model model,
			@RequestParam("fmemId") String fmemId,
			@RequestParam("accStatus") String accStatus,
			@RequestParam("accDescText") String accDesc) {
		
		Fmem fmem = fmemSvc.getOneByFmemId(Integer.valueOf(fmemId));
		
		fmem.setAccStatus(Byte.valueOf(accStatus));
		fmem.setAccDesc(accDesc);
		fmemSvc.updateFmem(fmem);
		return "redirect:/admin/reviewFmems";
	}
	
	
	
	
}
