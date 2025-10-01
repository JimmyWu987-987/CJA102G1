package com.farmtastic.fmember.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.eclipse.tags.shaded.org.apache.xalan.lib.Redirect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.fmember.model.FmemService;
import com.farmtastic.fmember.model.LoginRequest;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/fmem")
public class FmemController{
	
	@Autowired
	FmemService fmemSvc;
	
	// @InitBinder：用來註冊**資料綁定器**
	// 這個方法會在每次**處理請求之前**自動執行
	// WebDataBinder binder`：負責將 HTTP 請求參數綁定到 Java 物件
//	如果 @InitBinder 設定正確（allowEmpty = true），通常不會遇到 typeMismatch 錯誤，
//			因為空值會正確轉為 null，然後被 @NotNull 驗證捕捉
	@InitBinder  //檢查年齡滿XX歲
	public void inintBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
//	註冊頁面"超連結"
	@GetMapping("/showFmemRegLoginForm")
	public String showFmemRegLoginForm(ModelMap model) {
		model.addAttribute("fmem", new Fmem());
		model.addAttribute("loginRequest", new LoginRequest());
		return "front_end/farmer/unlogined/fmemRegLogin";
	}
	
//	送出註冊"表單"
	@PostMapping("/register")
	public String register(
			@Valid @ModelAttribute("fmem") 
			Fmem fmem, 
			BindingResult result, 
			ModelMap model, 
			RedirectAttributes redirectAttrs) {
	
		// 驗證帳號不能跟別人重複
		String fmemAcc = fmem.getFmemAcc();
		try {
			fmemSvc.Register(fmemAcc);
		} catch (IllegalStateException e) {
			model.addAttribute("regError", e.getMessage());
			model.addAttribute("loginRequest", new LoginRequest()); // 給login用
			model.addAttribute("activeTab", "register"); //標記目前所在頁籤
			return "front_end/farmer/unlogined/fmemRegLogin";
		}
		
		if (result.hasErrors()) {
			model.addAttribute("loginRequest", new LoginRequest()); // 給login用
			model.addAttribute("activeTab", "register"); //標記目前所在頁籤
			return "front_end/farmer/unlogined/fmemRegLogin";
		}
		
		fmemSvc.addFmem(fmem);
		
		redirectAttrs.addFlashAttribute("success", "小農會員註冊成功");
		return "redirect:/"; //要重導到小農首頁
	}
	
	
	@PostMapping("/login")
	public String login(LoginRequest loginRequest, HttpSession session, ModelMap model) {
		
		String fmemAccLogin = loginRequest.getFmemAccLogin();
		String fmemPwdLogin = loginRequest.getFmemPwdLogin();
		
		// 1.基本欄位驗證 
		if(fmemAccLogin == null || fmemAccLogin.trim().isEmpty()) {
			model.addAttribute("loginError", "請輸入帳號");
			model.addAttribute("loginRequest", loginRequest);
			model.addAttribute("fmem", new Fmem()); 
			model.addAttribute("activeTab", "login");  //標記目前所在頁籤
			return "front_end/farmer/unlogined/fmemRegLogin";
		}
		
		if(fmemPwdLogin == null || fmemPwdLogin.trim().isEmpty()) {
			model.addAttribute("loginError", "請輸入密碼");
			model.addAttribute("loginRequest", loginRequest);
			model.addAttribute("fmem", new Fmem());
			model.addAttribute("activeTab", "login");  //標記目前所在頁籤
			return "front_end/farmer/unlogined/fmemRegLogin";
		}
		
		// 2.呼叫service進行登入驗證
		try {
			Fmem fmem = fmemSvc.Login(fmemAccLogin, fmemPwdLogin);
			
			if(fmem == null) {
				model.addAttribute("loginError", "帳號或密碼錯誤");
				model.addAttribute("loginRequest", loginRequest);
				model.addAttribute("fmem", new Fmem());
				model.addAttribute("activeTab", "login");  //標記目前所在頁籤
				return "front_end/farmer/unlogined/fmemRegLogin";
			}
			
			
			// 3.登入成功，把會員資料存進session
			session.setAttribute("loggedInMember", fmem);
			session.setAttribute("fmemId", fmem.getFmemId());
			session.setAttribute("fmemName", fmem.getFmemName());
			
			// 4.登入成功後 重導至首頁或會員中心
			return "redirect:/fmem/home";
		} catch (IllegalStateException e) {
			model.addAttribute("loginError", e.getMessage());
			model.addAttribute("loginRequest", loginRequest);
			model.addAttribute("fmem", new Fmem()); // ???
			model.addAttribute("activeTab", "login");  //標記目前所在頁籤
			return "front_end/farmer/unlogined/fmemRegLogin";
		}
	}
	
	
	@PostMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("loggedInMember");
		return "redirect:/";
	}
	
	
}
