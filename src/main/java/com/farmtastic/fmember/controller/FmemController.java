package com.farmtastic.fmember.controller;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.fmember.model.FmemService;
import com.farmtastic.fmember.model.LoginRequest;
import com.farmtastic.fmember.model.UpdatePasswordFmem;
import com.farmtastic.fmember.model.UpdateProfileFmem;
import com.farmtastic.fmember.model.UpdateStoreFmem;
import com.farmtastic.member.model.Mem;
import com.farmtastic.member.model.UpdateProfileMem;
import com.farmtastic.validator.RegistrationValidation;
import com.farmtastic.validator.UpdatePasswordValidation;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/fmem")
@SessionAttributes({"loggedInFmember", "fmemTest"})
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
	
	
//	登入後的小農首頁
	@GetMapping("/home")
	public String fmemHome(HttpSession session) {
		Fmem fmem = (Fmem) session.getAttribute("loggedInFmember");
		if(fmem != null) {
			return "/front_end/farmer/logined/home";			
		} else {
			return "redirect:/fmem/showFmemRegLoginForm";
		}
	}
	
	
	@GetMapping("/toFmemArea")
	public String toFmemArea(HttpSession session){
		Fmem fmem = (Fmem) session.getAttribute("loggedInFmember");
		if(fmem != null) {
			return "redirect:/fmem/fmemArea";
		} else {
			return "redirect:/fmem/showFmemRegLoginForm";
		}
	}
	
	@GetMapping("/fmemArea")
	public String fmemArea() {
//		public String fmemArea(ModelMap model, HttpSession session) {
//		---------------
//		Fmem fmemTest = fmemSvc.getOneByFmemId(1).orElse(null);
//		session.setAttribute("fmemTest", fmemTest);
//		---------------
		return "/front_end/farmer/logined/fmemArea";
	}
	
	
	
	@GetMapping("/toUpdateProfile")
	public String toUpdateProfile(HttpSession session){
		Fmem fmem = (Fmem) session.getAttribute("loggedInFmember");
		if(fmem != null) {
			return "redirect:/fmem/fmemArea/updateProfilePage";
		} else {
			return "redirect:/fmem/showFmemRegLoginForm";
		}
	}
	
	@GetMapping("/fmemArea/updateProfilePage")
	public String updateProfilePage(
//			HttpSession session,
			@ModelAttribute("loggedInFmember") Fmem loggedInFmember,
			ModelMap model) {
		
		if (loggedInFmember.getFmemPic() != null) {
			String fmemPicBase64 = Base64.getEncoder().encodeToString(loggedInFmember.getFmemPic());
			model.addAttribute("fmemPicBase64", fmemPicBase64);
		}
		if (loggedInFmember.getOrganicPic() != null) {
			String organicPicBase64 = Base64.getEncoder().encodeToString(loggedInFmember.getOrganicPic());
			model.addAttribute("organicPicBase64", organicPicBase64);
		}
		if (loggedInFmember.getLandPic() != null) {
			String landPicBase64 = Base64.getEncoder().encodeToString(loggedInFmember.getLandPic());
			model.addAttribute("landPicBase64", landPicBase64);
		}
		if (loggedInFmember.getInsurPic() != null) {
			String insurPicBase64 = Base64.getEncoder().encodeToString(loggedInFmember.getInsurPic());
			model.addAttribute("insurPicBase64", insurPicBase64);
		}
		
		UpdateProfileFmem updateProfileFmem = new UpdateProfileFmem();
		BeanUtils.copyProperties(loggedInFmember, updateProfileFmem);
		model.addAttribute("updateProfileFmem", updateProfileFmem);
		return "/front_end/farmer/logined/fmemProfile/fmemUpdateProfile";
	}
	
//	修改個人資料 => 存進DB
	@PostMapping("/fmemArea/updateProfile")
	public String updateProfile(
			@Valid @ModelAttribute("updateProfileFmem") UpdateProfileFmem updateProfileFmem,
			BindingResult result, //一定要放在@Valid @ModelAttribute後面，不然如果有錯誤不會進controller
			@RequestParam(value = "fmemPic", required = false) MultipartFile fmemPicFile,
			@RequestParam(value = "organicPic", required = false) MultipartFile organicPicFile,
			@RequestParam(value = "landPic", required = false) MultipartFile landPicFile,
			@RequestParam(value = "insurPic", required = false) MultipartFile insurPicFile,
			ModelMap model,
			HttpSession session,
			RedirectAttributes redirectAttrs) throws IOException {
		
		if (result.hasErrors()) {
			return "/front_end/farmer/logined/fmemProfile/fmemUpdateProfile";
		}
		
		Fmem loggedInFmember = (Fmem) session.getAttribute("loggedInFmember");
		if (loggedInFmember == null) {
			return "redirect:/fmem/showFmemRegLoginForm";
		}

		BeanUtils.copyProperties(updateProfileFmem, loggedInFmember);
		
		if (fmemPicFile != null && !fmemPicFile.isEmpty()) {
			loggedInFmember.setFmemPic(fmemPicFile.getBytes());
		}
		if (organicPicFile != null && !organicPicFile.isEmpty()) {
			loggedInFmember.setOrganicPic(organicPicFile.getBytes());
		}
		if (landPicFile != null && !landPicFile.isEmpty()) {
			loggedInFmember.setLandPic(landPicFile.getBytes());
		}
		if (insurPicFile != null && !insurPicFile.isEmpty()) {
			loggedInFmember.setInsurPic(insurPicFile.getBytes());
		}
		
		fmemSvc.updateFmem(loggedInFmember);

		session.setAttribute("loggedInFmember", loggedInFmember); //index右上角顯示更新
		redirectAttrs.addFlashAttribute("success", "修改資料成功");
		return "redirect:/fmem/fmemArea/updateProfilePage";
	}
	
	
	
	
	
	@GetMapping("/toUpdatePassword")
	public String toUpdatePassword(HttpSession session){
		Fmem fmem = (Fmem) session.getAttribute("loggedInFmember");
		if(fmem != null) {
			return "redirect:/fmem/fmemArea/updatePasswordPage";
		} else {
			return "redirect:/fmem/showFmemRegLoginForm";
		}
	}
	
	@GetMapping("/fmemArea/updatePasswordPage")
	public String updatePasswordPage(
			@ModelAttribute("loggedInFmember") Fmem loggedInFmember,
			ModelMap model) {
		
		UpdatePasswordFmem updatePasswordFmem = new UpdatePasswordFmem();
		BeanUtils.copyProperties(loggedInFmember, updatePasswordFmem);
		model.addAttribute("updatePasswordFmem", updatePasswordFmem); //??
		return "/front_end/farmer/logined/fmemProfile/fmemUpdatePassword";
	}
	
	@PostMapping("/fmemArea/updatePassword")
	public String updatePassword(
			@Validated(UpdatePasswordValidation.class) @ModelAttribute("updatePasswordFmem") Fmem updatePasswordFmem,
			BindingResult result,
			ModelMap model,
			HttpSession session,
			RedirectAttributes redirectAttrs) {
		
		if (result.hasErrors()) {
			return "/front_end/farmer/logined/fmemProfile/fmemUpdatePassword";
		}
		
		Fmem loggedInFmember = (Fmem) session.getAttribute("loggedInFmember");
		if (loggedInFmember == null) {
			return "redirect:/fmem/showFmemRegLoginForm";
		}
		
		loggedInFmember.setFmemPwd(updatePasswordFmem.getFmemPwd());
		fmemSvc.updateFmem(loggedInFmember);
		redirectAttrs.addFlashAttribute("success", "修改密碼成功");
		return "redirect:/fmem/fmemArea/updatePasswordPage";
	}
	
	
	
	@GetMapping("/toUpdateStore")
	public String toUpdateStore(HttpSession session){
		Fmem fmem = (Fmem) session.getAttribute("loggedInFmember");
		if(fmem != null) {
			return "redirect:/fmem/fmemArea/updateStorePage";
		} else {
			return "redirect:/fmem/showFmemRegLoginForm";
		}
	}
	
	@GetMapping("/fmemArea/updateStorePage")
	public String updateStorePage(
//			HttpSession session,
			@ModelAttribute("loggedInFmember") Fmem loggedInFmember,
			ModelMap model) {
		
		if (loggedInFmember.getStorePic() != null) {
			String storePicBase64 = Base64.getEncoder().encodeToString(loggedInFmember.getStorePic());
			model.addAttribute("storePicBase64", storePicBase64);
		}
		
		UpdateStoreFmem updateStoreFmem = new UpdateStoreFmem();
		BeanUtils.copyProperties(loggedInFmember, updateStoreFmem);
		model.addAttribute("updateStoreFmem", updateStoreFmem);
		return "/front_end/farmer/logined/fmemProfile/fmemUpdateStore";
	}
	
//	修改資料 => 存進DB
	@PostMapping("/fmemArea/updateStore")
	public String updateStore(
			@Valid @ModelAttribute("updateStoreFmem") UpdateStoreFmem updateStoreFmem,
			BindingResult result, //一定要放在@Valid @ModelAttribute後面，不然如果有錯誤不會進controller
			@RequestParam(value = "storePic", required = false) MultipartFile storePicFile,
			ModelMap model,
			HttpSession session,
			RedirectAttributes redirectAttrs) throws IOException {
		
		if (result.hasErrors()) {
			return "/front_end/farmer/logined/fmemProfile/fmemUpdateStore";
		}
		
		Fmem loggedInFmember = (Fmem) session.getAttribute("loggedInFmember");
		if (loggedInFmember == null) {
			return "redirect:/fmem/showFmemRegLoginForm";
		}

		BeanUtils.copyProperties(updateStoreFmem, loggedInFmember);
		
		if (storePicFile != null && !storePicFile.isEmpty()) {
			loggedInFmember.setStorePic(storePicFile.getBytes());
		}
		
		fmemSvc.updateFmem(loggedInFmember);
		session.setAttribute("loggedInFmember", loggedInFmember); //index右上角顯示更新
		redirectAttrs.addFlashAttribute("success", "修改資料成功");
		System.out.println("成功修改");
		return "redirect:/fmem/fmemArea/updateStorePage";
	}
	
	
	
	
	
	
	
//	送出註冊"表單"
	@PostMapping("/register")
	public String register(
			@Validated(RegistrationValidation.class) @ModelAttribute("fmem") 
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
			model.addAttribute("loggedInFmember", fmem); //@SessionAttributes
			
			model.addAttribute("fmemId", fmem.getFmemId());
			model.addAttribute("fmemName", fmem.getFmemName());
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
	public String logout(HttpSession session, SessionStatus status) {
		if(!status.isComplete()) {
			status.setComplete();
		}
		session.removeAttribute("loggedInMember");
		return "redirect:/fmem/showFmemRegLoginForm";
	}
	
	
}
