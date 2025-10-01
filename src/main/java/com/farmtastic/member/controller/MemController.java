package com.farmtastic.member.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;

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

import com.farmtastic.member.model.LoginRequest;
import com.farmtastic.member.model.Mem;
import com.farmtastic.member.model.MemService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/mem")
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
	
//	送出註冊"表單"
	@PostMapping("/register")
	public String register(
			@Valid @ModelAttribute("mem") 
			Mem mem, 
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
			session.setAttribute("loggedInMember", mem);
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
	public String logout(HttpSession session) {
		session.removeAttribute("loggedInMember");
		return "redirect:/";
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
		
		
//		在Update頁面 送出修改資料
		if("update".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

//			---------1.接收請求參數--------

//			用來查出這筆會員資料
			Integer memId = Integer.valueOf(req.getParameter("memId"));
//			String memAcc = req.getParameter("memAcc"); 帳號不能改
			
//			密碼驗證
			String memPwd = req.getParameter("memPwd");
			String memPwdReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9@)]{8,20}$";
			if(memPwd == null || (memPwd.trim()).length() == 0) {
				errorMsgs.add("密碼欄位請勿空白");
			} else if(!memPwd.trim().matches(memPwdReg)) {
				errorMsgs.add("密碼格式不符，請輸入英文或數字或@ , 且長度必需在8到20之間");
			}
			
//			審核狀態(改下拉選單)***
			String accStatusStr =  req.getParameter("accStatus");
			Byte accStatus = Byte.valueOf(accStatusStr);
			
//			String accStatusStr =  req.getParameter("accStatus").trim();
//			Integer accStatus = null;
//			
//			if (accStatusStr.length() != 0) {
//				accStatus = Integer.valueOf(accStatusStr);				
//			}
//			
//			if (accStatusStr == null || accStatusStr.length() == 0) {
//				errorMsgs.add("審核狀態欄位請勿空白");
//			} else if(accStatus < 0) {
//				errorMsgs.add("審核狀態不能為負值");  //前端也有驗證
//			}
			
//			姓名驗證
			String memName = req.getParameter("memName");
			String memNameReg = "^[(\u4e00-\u9fa5)(a-zA-Z)]{2,20}$";
			if(memName == null || (memName.trim()).length() == 0) {
				errorMsgs.add("姓名欄位請勿空白");
			} else if(!memName.trim().matches(memNameReg)) {
				errorMsgs.add("姓名格式不符，請輸入中文或英文, 且長度必需在2到20之間");
			}
			
//			手機號碼驗證
			String memMobile = req.getParameter("memMobile");
			String memMobileReg = "^09[0-9]{2}-[0-9]{6}$";
			if(memMobile == null || (memMobile.trim()).length() == 0) {
				errorMsgs.add("手機欄位請勿空白");
			} else if (!memMobile.trim().matches(memMobileReg)) {
				errorMsgs.add("手機格式不符，範例: 0912-123456");
			}
			
//			信箱驗證
			String memEmail = req.getParameter("memEmail");
			String memEmailReg = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
			if(memEmail == null || (memEmail.trim()).length() == 0) {
				errorMsgs.add("信箱欄位請勿空白");
			} else if (!memEmail.trim().matches(memEmailReg)) {
				errorMsgs.add("信箱格式不符，範例: abcdefg1234@gmail.com");
			}
			
//			郵遞區號驗證
			String memZipcode = req.getParameter("memZipcode");
			String memZipcodeReg = "^[0-9]{3,6}$";
			if(memZipcode == null || (memZipcode.trim()).length() == 0) {
				errorMsgs.add("郵遞區號欄位請勿空白");
			} else if (!memZipcode.trim().matches(memZipcodeReg)) {
				errorMsgs.add("郵遞區號格式不符，請輸入3~6位數字");
			}
			
//			縣市驗證(改成下拉選單)***
			String memCity = req.getParameter("memCity");
			if(memCity == null || (memCity.trim()).length() == 0) {
				errorMsgs.add("縣市欄位請勿空白");
			}
			
//			區域驗證(改成下拉選單)***
			String memDist = req.getParameter("memDist");
			if(memDist == null || (memDist.trim()).length() == 0) {
				errorMsgs.add("區域欄位請勿空白");
			}
			
//			地址驗證
			String memAddr = req.getParameter("memAddr");
			String memAddrReg = "^[\u4e00-\u9fa5\\-\\d]{3,20}$";
			if(memAddr == null || (memAddr.trim()).length() == 0) {
				errorMsgs.add("地址欄位請勿空白");
			} else if (!memAddr.trim().matches(memAddrReg)) {
				errorMsgs.add("地址格式不符，請輸入中文或數字或-");
			}
//			**********
			
//			Timestamp regDate = Timestamp.valueOf(req.getParameter("regDate")); 註冊時間不能改
			
//			會員點數驗證
			String memPointStr = req.getParameter("memPoint").trim();
			Integer memPoint = null;
			
			if (memPointStr.length() != 0) {
				memPoint = Integer.valueOf(req.getParameter("memPoint").trim());
			}
			
			if (memPointStr == null || memPointStr.length() == 0) {
				errorMsgs.add("會員點數請勿空白");
			} else if (memPoint < 0) {
				errorMsgs.add("會員點數不能為負值");  
			}
			
	
			MemService memSvc = new MemService();
		    Mem mem = memSvc.getOneMem(memId);
		    
//			mem.setMemId(memId);
//			mem.setMemAcc(memAcc);
			mem.setMemPwd(memPwd);
			mem.setMemName(memName);
			mem.setAccStatus(accStatus);
			mem.setMemMobile(memMobile);
			mem.setMemEmail(memEmail);
			mem.setMemZipcode(memZipcode);
			mem.setMemCity(memCity);
			mem.setMemDist(memDist);
			mem.setMemAddr(memAddr);
//			mem.setRegDate(regDate);
			mem.setMemPoint(memPoint);
			
			
			if(!errorMsgs.isEmpty()) {
				req.setAttribute("mem", mem);
				String url = "/back-end/mem/update_mem_input.jsp";
				RequestDispatcher failureView = req.getRequestDispatcher(url);
				failureView.forward(req, res);
				return;
			}
			
			
//			---------2.開始更新資料--------
			memSvc.updateMem(mem);
			
//			---------3.更新完成，跳到更新成功的頁面--------
//			req.setAttribute("mem", mem);
			String url = "/back-end/mem/listAllMem.jsp";
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

}
