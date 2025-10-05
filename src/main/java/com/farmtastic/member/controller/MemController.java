package com.farmtastic.member.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.member.model.LoginRequest;
import com.farmtastic.member.model.Mem;
import com.farmtastic.member.model.MemService;
import com.farmtastic.member.model.UpdatePasswordMem;
import com.farmtastic.member.model.UpdateProfileMem;
import com.farmtastic.validator.RegistrationValidation;
import com.farmtastic.validator.UpdatePasswordValidation;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/mem")
@SessionAttributes("loggedInMember")  //只要有model.addAttribute("mem", mem) => 會自動把mem放進session裡

public class MemController{
	
	@Autowired
	MemService memSvc;
	
	// @InitBinder：用來註冊**資料綁定器**
	// 這個方法會在每次**處理請求之前**自動執行
	// WebDataBinder binder`：負責將 HTTP 請求參數綁定到 Java 物件
//	如果 @InitBinder 設定正確（allowEmpty = true），通常不會遇到 typeMismatch 錯誤，
//			因為空值會正確轉為 null，然後被 @NotNull 驗證捕捉
	@InitBinder  //檢查年齡滿XX歲
	public void inintBinder(WebDataBinder binder) {
//		這會告訴 Spring 如何解析前端傳來的日期字串
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// **true 表示允許空值，空值會傳 null 而不是拋出轉換錯誤
		dateFormat.setLenient(false);
		                                     // CustomDateEditor：Spring 提供的日期編輯器
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
//	註冊頁面"超連結"
	@GetMapping("/showMemRegLoginForm")
	public String showMemRegLoginForm(ModelMap model) {
		model.addAttribute("mem", new Mem());
		model.addAttribute("loginRequest", new LoginRequest());
		return "front_end/customer/unlogined/memRegLogin";
	}
	
	@GetMapping("/toMemArea")
	public String toMemArea(HttpSession session){
		Mem mem = (Mem) session.getAttribute("loggedInMember");
		if(mem != null) {
			return "redirect:/mem/memArea";
		} else {
			return "redirect:/mem/showMemRegLoginForm";
		}
	}
	
	
//	登入後才能看的: 會員專區
	@GetMapping("/memArea")
	public String memArea() {
		return "/front_end/customer/logined/memArea";
	}

	

	
	@GetMapping("/toUpdateProfile")
	public String toUpdateProfile(HttpSession session){
		Mem mem = (Mem) session.getAttribute("loggedInMember");
		if(mem != null) {
			return "redirect:/mem/memArea/updateProfilePage";
		} else {
			return "redirect:/mem/showMemRegLoginForm";
		}
	}
	
	
//	怎麼分辨是「表單送來的」還是「Session 裡的」？
//	Spring 的處理順序大致是這樣：
//	1. 如果是 @PostMapping，且有 th:object="loggedInMember"，那麼 Spring 會用 表單資料來綁定 loggedInMember
//	2. 如果你沒有送這個物件（或是 GET 請求），那麼 Spring 就會從 @SessionAttributes 管理的 session model 中取出 loggedInMember 填給你
//	登入後才能看的: 會員專區/修改個人資料頁面
	@GetMapping("/memArea/updateProfilePage")
	public String updateProfilePage(
//			HttpSession session,
			@ModelAttribute("loggedInMember") Mem loggedInMember,
			ModelMap model) {
		
//		Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");
//		if (loggedInMember == null) {
//			return "redirect:/mem/showMemRegLoginForm";
//		}
		
		UpdateProfileMem updateProfileMem = new UpdateProfileMem();
		BeanUtils.copyProperties(loggedInMember, updateProfileMem);
		model.addAttribute("updateProfileMem", updateProfileMem);
		return "/front_end/customer/logined/memProfile/memUpdateProfile";
	}
	
//	修改個人資料 => 存進DB
	@PostMapping("/memArea/updateProfile")
	public String updateProfile(
			@Valid @ModelAttribute("updateProfileMem") UpdateProfileMem updateProfileMem,
			BindingResult result, //一定要放在@Valid @ModelAttribute後面，不然如果有錯誤不會進controller
			ModelMap model,
			HttpSession session,
			RedirectAttributes redirectAttrs) {
		
		if (result.hasErrors()) {
			return "/front_end/customer/logined/memProfile/memUpdateProfile";
		}
		
		Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");
		if (loggedInMember == null) {
			return "redirect:/mem/showMemRegLoginForm";
		}

		BeanUtils.copyProperties(updateProfileMem, loggedInMember);
		memSvc.updateMem(loggedInMember);
		System.out.println("loggedInMember="+loggedInMember);
//		model.addAttribute("loggedInMember", loggedInMember); //index右上角顯示更新
		session.setAttribute("loggedInMember", loggedInMember); //index右上角顯示更新
		redirectAttrs.addFlashAttribute("success", "修改資料成功");
		return "redirect:/mem/memArea/updateProfilePage";
	}
	
	
	
	
	@GetMapping("/toUpdatePassword")
	public String toUpdatePassword(HttpSession session){
		Mem mem = (Mem) session.getAttribute("loggedInMember");
		if(mem != null) {
			return "redirect:/mem/memArea/updatePasswordPage";
		} else {
			return "redirect:/mem/showMemRegLoginForm";
		}
	}
	
	@GetMapping("/memArea/updatePasswordPage")
	public String updatePasswordPage(
			@ModelAttribute("loggedInMember") Mem loggedInMember,
			ModelMap model) {
		
		UpdatePasswordMem updatePasswordMem = new UpdatePasswordMem();
		BeanUtils.copyProperties(loggedInMember, updatePasswordMem);
		model.addAttribute("updatePasswordMem", updatePasswordMem); //??
		return "/front_end/customer/logined/memProfile/memUpdatePassword";
	}
	
	@PostMapping("/memArea/updatePassword")
	public String updatePassword(
			@Validated(UpdatePasswordValidation.class) @ModelAttribute("updatePasswordMem") Mem updatePasswordMem,
			BindingResult result, //一定要放在@Valid @ModelAttribute後面，不然如果有錯誤不會進controller
			ModelMap model,
			HttpSession session,
			RedirectAttributes redirectAttrs) {
		
		if (result.hasErrors()) {
			return "/front_end/customer/logined/memProfile/memUpdatePassword";
		}
		
		Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");
		if (loggedInMember == null) {
			return "redirect:/mem/showMemRegLoginForm";
		}

		loggedInMember.setMemPwd(updatePasswordMem.getMemPwd());
//		BeanUtils.copyProperties(updatePasswordMem, loggedInMember);
		memSvc.updateMem(loggedInMember);
		redirectAttrs.addFlashAttribute("success", "修改密碼成功");
		return "redirect:/mem/memArea/updatePasswordPage";
	}
	
	
	
	

//	送出註冊"表單"
	@PostMapping("/register")
	public String register(
			@Validated(RegistrationValidation.class) @ModelAttribute("mem") Mem mem, 
			BindingResult result, 
			ModelMap model,
			RedirectAttributes redirectAttrs) {
	
		// 驗證帳號不能跟別人重複
		String memAcc = mem.getMemAcc();
		try {
			memSvc.Register(memAcc);
		} catch (IllegalStateException e) {
			model.addAttribute("regError", e.getMessage());
			model.addAttribute("loginRequest", new LoginRequest()); // 給login用
			model.addAttribute("activeTab", "register"); //標記目前所在頁籤
			return "front_end/customer/unlogined/memRegLogin";
		}
		
		if (result.hasErrors()) {
			model.addAttribute("loginRequest", new LoginRequest()); // 給login用
			model.addAttribute("activeTab", "register"); //標記目前所在頁籤
			return "front_end/customer/unlogined/memRegLogin";
		}
		
		memSvc.addMem(mem);
		
		redirectAttrs.addFlashAttribute("success", "註冊成功");
		return "redirect:/"; //註冊(新增)成功後重導至index.html
	}
	
	
	@PostMapping("/login")
	public String login(LoginRequest loginRequest, HttpSession session, ModelMap model) {
		String memAccLogin = loginRequest.getMemAccLogin();
		String memPwdLogin = loginRequest.getMemPwdLogin();
		
		// 1.基本欄位驗證 
		if(memAccLogin == null || memAccLogin.trim().isEmpty()) {
			model.addAttribute("loginError", "請輸入帳號");
			model.addAttribute("loginRequest", loginRequest);
			model.addAttribute("mem", new Mem()); // 給註冊表單用???
			model.addAttribute("activeTab", "login");  //標記目前所在頁籤
			return "front_end/customer/unlogined/memRegLogin";
		}
		if(memPwdLogin == null || memPwdLogin.trim().isEmpty()) {
			model.addAttribute("loginError", "請輸入密碼");
			model.addAttribute("loginRequest", loginRequest);
			model.addAttribute("mem", new Mem()); // ???
			model.addAttribute("activeTab", "login");  //標記目前所在頁籤
			return "front_end/customer/unlogined/memRegLogin";
		}
		
		// 2.呼叫service進行登入驗證
		try {
			Mem mem = memSvc.Login(memAccLogin, memPwdLogin);
			
			if(mem == null) {
				model.addAttribute("loginError", "帳號或密碼錯誤");
				model.addAttribute("loginRequest", loginRequest);
				model.addAttribute("mem", new Mem()); // ???
				model.addAttribute("activeTab", "login");  //標記目前所在頁籤
				return "front_end/customer/unlogined/memRegLogin";
			}
			
			// 3.登入成功，把會員資料存進session
			
			model.addAttribute("loggedInMember", mem);

			model.addAttribute("memId", mem.getMemId());
			model.addAttribute("memName", mem.getMemName());
			
//			session.setAttribute("loggedInMember", mem);  //@SessionAttributes
			session.setAttribute("memId", mem.getMemId());
			session.setAttribute("memName", mem.getMemName());
			
			// 4.登入成功後 重導至首頁
//			return "redirect:/mem/memArea";
			return "redirect:/";
		} catch (IllegalStateException e) {
			model.addAttribute("loginError", e.getMessage());
			model.addAttribute("loginRequest", loginRequest);
			model.addAttribute("mem", new Mem()); // ???
			model.addAttribute("activeTab", "login");  //標記目前所在頁籤
			return "front_end/customer/unlogined/memRegLogin";
		}
		
	}

	
	@PostMapping("/logout")
	public String logout(HttpSession session, SessionStatus status) {
		// 1. 清掉 @SessionAttributes 管理的 model 屬性
		if(!status.isComplete()) {
			status.setComplete();
		}
		
		// 2. 清掉 HttpSession 屬性
		session.removeAttribute("loggedInMember");
		
		// 3. 重導到首頁
		return "redirect:/";
	}

}
	
	/*

	
//		用審核狀態查詢多筆會員資料*****
		
		HttpSession session = req.getSession();
		if("getMulti_For_Display".equals(action)) {
			String accStatusStr = req.getParameter("accStatus");
			
			// form表單用accStatus查詢時 session保存參數
			session.setAttribute("accStatusStr", accStatusStr);
			
			
			Byte accStatus = Byte.valueOf(accStatusStr);
			MemService memSvc = new MemService();
			List<Mem> memList = memSvc.getMems(accStatus);
			
			
			req.setAttribute("memList", memList);
			String url = "/back-end/mem/listMems.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url);
			successView.forward(req, res);
			
		}
		
		if("getMulti_For_Display2".equals(action)) {
			
//			HttpSession session = req.getSession();
			String accStatusStr = (String) session.getAttribute("accStatusStr");
		
			Byte accStatus = Byte.valueOf(accStatusStr);
			
			MemService memSvc = new MemService();
			List<Mem> memList = memSvc.getMems(accStatus);
			
			
			req.setAttribute("memList", memList);
			String url = "/back-end/mem/listMems.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url);
			successView.forward(req, res);
			
		}
		
		

//		///////////////////  先查詢出單筆資料 再跳到Update頁面
		if("getOne_For_Update".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
//			---------1.接收請求參數--------
			Integer memId = Integer.valueOf(req.getParameter("memId"));
			
//			---------2.查詢出要更新的資料--------
			MemService memSvc = new MemService();
			Mem mem = memSvc.getOneMem(memId);
			
//			---------3.查詢完成，跳到修改頁面--------
			req.setAttribute("mem", mem);
			
			
			String url = "/back-end/mem/update_mem_input.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url);
			successView.forward(req, res);
			
		}
		
		
//		刪除會員資料(用會員編號)
		if("delete".equals(action)) {
			String str = req.getParameter("memId");
			Integer memId = Integer.valueOf(str);
			MemService memSvc = new MemService();
			memSvc.deleteMem(memId);
			
			String queryPage = req.getParameter("queryPage");
			String url = null;
//			if("listOneMem".equals(queryPage)) {
//				url = "/back-end/mem/listOneMem.jsp";
//			} else if ("listMems".equals(queryPage)) {
//				url = "/back-end/mem/listMems.jsp";				
//			} else {
				url = "/back-end/mem/listAllMem.jsp";				
//			}
			
			RequestDispatcher successView = req.getRequestDispatcher(url);
			successView.forward(req, res);
			
		}
		
	}*/

