package com.farmtastic.admin.controller;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

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
import com.farmtastic.redis.verification.MailService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminMemController {
	
	@Autowired
	MemService memSvc = new MemService();
	
	@Autowired
	FmemService fmemSvc = new FmemService();
	
	@Autowired
	MailService mailSvc;
	
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
			HttpServletRequest request,
			@RequestParam("memId") Integer memId,
			@RequestParam("accStatus") Byte accStatus,
			RedirectAttributes redirectAttrs) {
		
		memSvc.updateAccStatus(memId, accStatus);
		
		List<Mem> listMem = memSvc.getAll();
		model.addAttribute("listMem", listMem);
		
		String mailTitle = null;
		String mailContent = null;
		String baseUrl = request.getScheme() + "://" + request.getServerName() + 
						 ( (request.getServerPort() == 80 || request.getServerPort() == 443) ? "" : ":" + request.getServerPort() );
		
		Integer accStatusInteger = Integer.valueOf(accStatus);
		switch(accStatusInteger) {
			case 1:
				mailTitle = "農作物與它們的產地：會員帳號-復權通知";
				mailContent = "您的帳號已恢復，可以重新開始使用：\n"
						+ baseUrl + "/mem/showMemRegLoginForm\n"
						+ "可由此連結登入網頁。";
				break;
			
			case 2:
				mailTitle = "農作物與它們的產地：會員帳號-停權通知";
				mailContent = "帳號已被停權：\n"
							  + "若有任何問題，請與平台聯繫，謝謝。\n";
				break;
		}
		
		if (accStatusInteger == 1 || accStatusInteger == 2) {
			Mem mem = memSvc.getOneByMemId(memId);
			mailSvc.sendMail(mem.getMemEmail(), mailTitle, mailContent);			
		}
		
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
			HttpServletRequest request,
			Model model, 
			@RequestParam("fmemId") Integer fmemId,
			@RequestParam("accStatus") Byte accStatus,
			RedirectAttributes redirectAttrs) {
		
		fmemSvc.updateAccStatus(fmemId, accStatus);
		
		List<Fmem> listFmem = fmemSvc.getAll();
		model.addAttribute("listFmem", listFmem);
		
		Integer accStatusInteger = Integer.valueOf(accStatus);
		String mailTitle = null;
		String mailContent = null;
		String baseUrl = request.getScheme() + "://" + request.getServerName() + 
						 ( (request.getServerPort() == 80 || request.getServerPort() == 443) ? "" : ":" + request.getServerPort() );
		
		switch(accStatusInteger) {
			case 2:
				mailTitle = "農作物與它們的產地：小農會員-復權通知";
				mailContent = "您的帳號已恢復，可以重新開始販售商品：\n"
						+ baseUrl + "/fmem/showFmemRegLoginForm\n\n"
						+ "可由此連結登入小農會員。";
				break;
			
			case 4:
				mailTitle = "農作物與它們的產地：小農會員-停權通知";
				mailContent = "帳號已被停權：\n"
							  + "若有任何問題，請與平台聯繫，謝謝。\n\n";
				break;
		}
		
		if (accStatusInteger == 2 || accStatusInteger == 4) {
			Fmem fmem = fmemSvc.getOneByFmemId(fmemId);
			mailSvc.sendMail(fmem.getFmemEmail(), mailTitle, mailContent);			
		}
		
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
			HttpServletRequest request,
			@RequestParam("fmemId") String fmemId,
			@RequestParam("accStatus") String accStatus,
			@RequestParam(value = "accDesc", required = false) String accDesc,
			@RequestParam(value = "accDescText", required = false) String accDescText) {
		
		Fmem fmem = fmemSvc.getOneByFmemId(Integer.valueOf(fmemId));
		
		fmem.setAccStatus(Byte.valueOf(accStatus));
		if("其他".equals(accDesc)) {
			fmem.setAccDesc(accDescText);
		} else {
			fmem.setAccDesc(accDesc);
		}
		
		fmemSvc.updateFmem(fmem);
		
		Integer accStatusInteger = Integer.valueOf(accStatus);
		String mailTitle = null;
		String mailContent = null;
		String baseUrl = request.getScheme() + "://" + request.getServerName() + 
						 ( (request.getServerPort() == 80 || request.getServerPort() == 443) ? "" : ":" + request.getServerPort() );
		
		switch(accStatusInteger) {
			case 1:
				mailTitle = "農作物與它們的產地：小農會員-審核通過";
				mailContent = "帳號已通過審核！\n"
							  + baseUrl + "/fmem/showFmemRegLoginForm\n"
							  + "可由此連結登入網站。";
				break;
			case 3:
				mailTitle = "農作物與它們的產地：小農會員-審核未過";
				mailContent = "帳號未通過審核原因：" + accDescText + "\n"
							  + baseUrl + "/fmem/supplementIdentityCheckPage\n"
							  + "可由此連結補件，或由小農登入頁面下方點擊「我要補件」。";
				break;
		}
		
		if (accStatusInteger == 1 || accStatusInteger == 3) {
			mailSvc.sendMail(fmem.getFmemEmail(), mailTitle, mailContent);			
		}
		
		return "redirect:/admin/reviewFmems";
	}
	
	
	
	
}
