package com.farmtastic.member.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.act.model.Act;
import com.farmtastic.act.model.ActService;
import com.farmtastic.common.constants.CpnConstants;
import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.fmember.model.FmemService;
import com.farmtastic.member.erum.AuthProvider;
import com.farmtastic.member.model.ForgetPwdRequest;
import com.farmtastic.member.model.LoginRequest;
import com.farmtastic.member.model.Mem;
import com.farmtastic.member.model.MemService;
import com.farmtastic.member.model.UpdatePasswordMem;
import com.farmtastic.member.model.UpdateProfileMem;
import com.farmtastic.memprocpn.model.MemProCpnServiceImp;
import com.farmtastic.pro.model.Pro;
import com.farmtastic.pro.model.ProService;
import com.farmtastic.proimage.model.ProImage;
import com.farmtastic.proimage.model.ProImageService;
import com.farmtastic.redis.verification.MailService;
import com.farmtastic.redis.verification.RedisService;
import com.farmtastic.validator.RegistrationValidation;
import com.farmtastic.validator.UpdatePasswordValidation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/mem")
@SessionAttributes("loggedInMember") // 只要有model.addAttribute("mem", mem) => 會自動把mem放進session裡

public class MemController {

	@Autowired
	MemService memSvc;

	@Autowired
	FmemService fmemSvc;
	
	@Autowired
	ActService actSvc;
	
	@Autowired
	ProService proSvc;
	
	@Autowired
	ProImageService proImageSvc;

	@Autowired
	RedisService redisSvc;

	@Autowired
	MailService mailSvc;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	MemProCpnServiceImp memProCpnSvc;

	// @InitBinder：用來註冊**資料綁定器**
	// 這個方法會在每次**處理請求之前**自動執行
	// WebDataBinder binder`：負責將 HTTP 請求參數綁定到 Java 物件
//	如果 @InitBinder 設定正確（allowEmpty = true），通常不會遇到 typeMismatch 錯誤，
//			因為空值會正確轉為 null，然後被 @NotNull 驗證捕捉
	@InitBinder // 檢查年齡滿XX歲
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
	public String showMemRegLoginForm(
			// from OAuth2AuthenticationFailureHandler
			// String redirectUrl = "/mem/showMemRegLoginForm?googleError=" + encodedError;
			@RequestParam(value = "googleError", required = false) String googleError,
			ModelMap model) {
		model.addAttribute("mem", new Mem());
		model.addAttribute("loginRequest", new LoginRequest());
		
		if(googleError != null && !googleError.isEmpty()) {
			// 對應到前端顯示
			model.addAttribute("googleError", googleError);
		}
		
		// 計算 12 歲前的日期
	    LocalDate maxDate = LocalDate.now().minusYears(12);
	    model.addAttribute("maxBirthday", maxDate.toString());
		
		return "front_end/customer/unlogined/memRegLogin";
	}

	@GetMapping("/farmerStoreProd")
	public String farmerStoreProd(ModelMap model, @RequestParam("fmemId") String fmemId, HttpSession session) {
		Integer fmemIdInteger = Integer.valueOf(fmemId);
		Fmem fmem = fmemSvc.getOneByFmemId(fmemIdInteger);

		List<Pro> proList = proSvc.findByFmemId(fmemIdInteger);
		for(Pro pro : proList) {
			Integer proId = pro.getProId();
			Optional<ProImage> proImage = proImageSvc.findFirstImageByProId(Long.valueOf(proId));
			
			if(proImage.isPresent()) {
				String proImageBase64 = Base64.getEncoder().encodeToString(proImage.orElse(null).getProImg());
				pro.setProImageBase64(proImageBase64);
			}
		}
		model.addAttribute("proList", proList);
		
		String StorePicBase64 = Base64.getEncoder().encodeToString(fmem.getStorePic());
		String fmemPicBase64 = Base64.getEncoder().encodeToString(fmem.getFmemPic());

		model.addAttribute("fmem", fmem);
		model.addAttribute("fmemId", fmemId);
		model.addAttribute("StorePicBase64", StorePicBase64);
		model.addAttribute("fmemPicBase64", fmemPicBase64);
		return "front_end/customer/unlogined/farmerStoreProd";
	}

	@GetMapping("/farmerStoreAct")
	public String farmerStoreAct(ModelMap model, @RequestParam("fmemId") String fmemId) {
		Integer fmemIdInteger = Integer.valueOf(fmemId);
		Fmem fmem = fmemSvc.getOneByFmemId(fmemIdInteger);
		
		List<Act> actList = actSvc.findByFmemId(fmemIdInteger, Sort.by(Sort.Direction.DESC, "actLaunUpd"));
		for(Act act : actList) {
			if(act.getActMainImg() != null) {
				act.setActMainImgBase64(Base64.getEncoder().encodeToString(act.getActMainImg()));
			}
		}
		model.addAttribute("actList", actList);
		
		String StorePicBase64 = Base64.getEncoder().encodeToString(fmem.getStorePic());
		String fmemPicBase64 = Base64.getEncoder().encodeToString(fmem.getFmemPic());

		model.addAttribute("fmem", fmem);
		model.addAttribute("fmemId", fmemId);
		model.addAttribute("StorePicBase64", StorePicBase64);
		model.addAttribute("fmemPicBase64", fmemPicBase64);
		return "front_end/customer/unlogined/farmerStoreAct";
	}

//	登入後才能看的: 會員專區
	@GetMapping("/memArea")
	public String memArea() {
		return "/front_end/customer/logined/memArea";
	}
	
	
	@GetMapping("/memArea/completeProfilePage")
	public String completeProfilePage(
			@ModelAttribute("loggedInMember") Mem loggedInMember,
	        ModelMap model,
	        HttpSession session) {
		
		if(loggedInMember.getMemBirthday() != null) {
			return "redirect:/mem/memArea";
		}
		model.addAttribute("loggedInMember", loggedInMember);
		return "front_end/customer/logined/memProfile/memCompleteProfile";
	}
	
	@PostMapping("/memArea/completeProfile")
	public String completeProfile(
	        @RequestParam("memBirthday") Date memBirthday,
	        @ModelAttribute("loggedInMember") Mem loggedInMember,
	        HttpSession session,
	        ModelMap model,
	        RedirectAttributes redirectAttrs) {
	    
	    // 驗證生日
	    if (memBirthday == null) {
	        model.addAttribute("error", "請填寫生日");
	        model.addAttribute("loggedInMember", loggedInMember);
	        return "front_end/customer/logined/memProfile/memCompleteProfile";
	    }
	    
	    // 檢查年齡（至少12歲）
	    long ageInMillis = System.currentTimeMillis() - memBirthday.getTime();
	    int age = (int) (ageInMillis / (365.25 * 24 * 60 * 60 * 1000));
	    if (age < 12) {
	        model.addAttribute("error", "您必須年滿 12 歲");
	        model.addAttribute("loggedInMember", loggedInMember);
	        return "front_end/customer/logined/memProfile/memCompleteProfile";
	    }
	    // 更新資料
	    loggedInMember.setMemBirthday(memBirthday);
	    memSvc.updateMem(loggedInMember);
	    
	    session.setAttribute("loggedInMember", loggedInMember);
	    redirectAttrs.addFlashAttribute("success", "生日填寫完成");
	    return "redirect:/mem/memArea";
	}
	
	
	

//	怎麼分辨是「表單送來的」還是「Session 裡的」？
//	Spring 的處理順序：
//	1. 如果是 @PostMapping，且有 th:object="loggedInMember"，那麼 Spring 會用 表單資料來綁定 loggedInMember
//	2. 如果你沒有送這個物件（或是 GET 請求），那麼 Spring 就會從 @SessionAttributes 管理的 session model 中取出 loggedInMember 填給你
//	登入後才能看的: 會員專區/修改個人資料頁面
	@GetMapping("/memArea/updateProfilePage")
	public String updateProfilePage(
			HttpSession session, 
			@ModelAttribute("loggedInMember") Mem loggedInMember,
			ModelMap model) {

		UpdateProfileMem updateProfileMem = new UpdateProfileMem();
		BeanUtils.copyProperties(loggedInMember, updateProfileMem);
		model.addAttribute("updateProfileMem", updateProfileMem);
		return "/front_end/customer/logined/memProfile/memUpdateProfile";
	}

//	修改個人資料 => 存進DB
	@PostMapping("/memArea/updateProfile")
	public String updateProfile(@Valid @ModelAttribute("updateProfileMem") UpdateProfileMem updateProfileMem,
			BindingResult result, // 一定要放在@Valid @ModelAttribute後面，不然如果有錯誤不會進controller
			ModelMap model, HttpSession session, RedirectAttributes redirectAttrs) {

		if (result.hasErrors()) {
			return "/front_end/customer/logined/memProfile/memUpdateProfile";
		}

		Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");
		if (loggedInMember == null) {
			return "redirect:/mem/showMemRegLoginForm";
		}

		BeanUtils.copyProperties(updateProfileMem, loggedInMember);
		memSvc.updateMem(loggedInMember);
		session.setAttribute("loggedInMember", loggedInMember); // index右上角顯示更新
		redirectAttrs.addFlashAttribute("success", "修改資料成功");
		return "redirect:/mem/memArea/updateProfilePage";
	}

	@GetMapping("/memArea/updatePasswordPage")
	public String updatePasswordPage(@ModelAttribute("loggedInMember") Mem loggedInMember, ModelMap model) {

		UpdatePasswordMem updatePasswordMem = new UpdatePasswordMem();
		BeanUtils.copyProperties(loggedInMember, updatePasswordMem);
		model.addAttribute("updatePasswordMem", updatePasswordMem);
		return "/front_end/customer/logined/memProfile/memUpdatePassword";
	}

	@PostMapping("/memArea/updatePassword")
	public String updatePassword(
			@Validated(UpdatePasswordValidation.class) @ModelAttribute("updatePasswordMem") Mem updatePasswordMem,
			BindingResult result, // 一定要放在@Valid @ModelAttribute後面，不然如果有錯誤不會進controller
			ModelMap model, HttpSession session, RedirectAttributes redirectAttrs) {

		if (result.hasErrors()) {
			return "/front_end/customer/logined/memProfile/memUpdatePassword";
		}

		Mem loggedInMember = (Mem) session.getAttribute("loggedInMember");
		if (loggedInMember == null) {
			return "redirect:/mem/showMemRegLoginForm";
		}

		loggedInMember.setMemPwd(passwordEncoder.encode(updatePasswordMem.getMemPwd()));
		memSvc.updateMem(loggedInMember);
		redirectAttrs.addFlashAttribute("success", "修改密碼成功");
		return "redirect:/mem/memArea/updatePasswordPage";
	}

//	送出註冊"表單"
	@PostMapping("/register")
	public String register(HttpServletRequest request,
			@Validated(RegistrationValidation.class) @ModelAttribute("mem") Mem mem, BindingResult result,
			ModelMap model, RedirectAttributes redirectAttrs) {

		// 驗證帳號(local/google分開)、手機、信箱不能跟別人重複
		String memAcc = mem.getMemAcc();
		String memMobile = mem.getMemMobile();
		String memEmail = mem.getMemEmail();
		
		if (memSvc.existsByMemAcc(memAcc)) {
			Mem memUsed = memSvc.getOneByMemAcc(memAcc); 
			if (memUsed.getAuthProvider() == AuthProvider.LOCAL) { //如果local沒註冊過，可以用(把google和local註冊帳號分開)
				result.rejectValue("memAcc", null, "此帳號已有人註冊過");
			}
		}
		if (memSvc.existsByMemMobile(memMobile)) {
			result.rejectValue("memMobile", null, "此手機已有人註冊過");
		}
		if (memSvc.existsByMemEmail(memEmail)) {
			result.rejectValue("memEmail", null, "此信箱已有人註冊過");
		}

		if (result.hasErrors()) {
			model.addAttribute("loginRequest", new LoginRequest()); // 給login用
			model.addAttribute("activeTab", "register"); // 標記目前所在頁籤
			return "front_end/customer/unlogined/memRegLogin";
		}

		memSvc.addMem(mem);

		// Redis 驗證碼
		String verificationCode = UUID.randomUUID().toString();
		redisSvc.setVerificationCode(verificationCode, mem.getMemAcc(), 60);

		String baseUrl = request.getScheme() + "://" + request.getServerName()
				+ ((request.getServerPort() == 80 || request.getServerPort() == 443) ? ""
						: ":" + request.getServerPort());
		String verifyUrl = "開通帳號請點擊此連結: \n" + baseUrl + "/mem/verifyEmail?code=" + verificationCode;
		mailSvc.sendMail(mem.getMemEmail(), "帳號開通信", verifyUrl);
		// 註冊成功後立即發券
		memProCpnSvc.giveCoupon(mem.getMemId(), CpnConstants.REGISTER_DISCOUNT_ID);
		memProCpnSvc.giveCoupon(mem.getMemId(), CpnConstants.REGISTER_CASHBACK_ID);
		redirectAttrs.addFlashAttribute("success", "註冊成功");
		return "redirect:/"; // 註冊(新增)成功後重導至index.html
	}

	@GetMapping("/verifyEmail")
	public String verifyEmail(@RequestParam("code") String code, ModelMap model, RedirectAttributes redirectAttrs) {

		String memAcc = redisSvc.getMemEmailByCode(code);
		if (memAcc == null) {
			model.addAttribute("fail", "驗證碼失效或不存在");
			return "redirect:/";
		}

		Mem mem = memSvc.getOneByMemAccAndAuthProvider(memAcc, AuthProvider.LOCAL);
		if (mem != null) {
			mem.setAccStatus((byte) 1);
			memSvc.updateMem(mem);

			redirectAttrs.addFlashAttribute("success", "驗證成功，帳號已啟用");
			redisSvc.deleteCode(code);
			return "redirect:/mem/showMemRegLoginForm";
		}
		model.addAttribute("fail", "使用者不存在");
		return "redirect:/";
	}

//	------------------忘記密碼----------------

	@GetMapping("/forgetPasswordPage")
	public String forgetPasswordPage(ModelMap model) {
		model.addAttribute("forgetPwdRequest", new ForgetPwdRequest());
		return "front_end/customer/unlogined/memForgetPassword";
	}

	@PostMapping("/forgetPassword")
	public String forgetPassword(HttpServletRequest request, ForgetPwdRequest forgetPwdRequest, HttpSession session,
			ModelMap model, RedirectAttributes redirectAttrs) {
		String memMobileForgetPwd = forgetPwdRequest.getMemMobileForgetPwd();
		String memEmailForgetPwd = forgetPwdRequest.getMemEmailForgetPwd();

		// 1.基本欄位驗證
		if (memMobileForgetPwd == null || memMobileForgetPwd.trim().isEmpty()) {
			model.addAttribute("forgetPwdError", "請輸入手機");
			model.addAttribute("forgetPwdRequest", forgetPwdRequest);
			return "front_end/customer/unlogined/memForgetPassword";
		}
		if (memEmailForgetPwd == null || memEmailForgetPwd.trim().isEmpty()) {
			model.addAttribute("forgetPwdError", "請輸入信箱");
			model.addAttribute("forgetPwdRequest", forgetPwdRequest);
			return "front_end/customer/unlogined/memForgetPassword";
		}

		// 2.呼叫service進行驗證
		try {
			Mem mem = memSvc.forgetPassword(memMobileForgetPwd, memEmailForgetPwd);
			if (mem == null) {
				model.addAttribute("forgetPwdError", "查無此帳號");
				model.addAttribute("forgetPwdRequest", forgetPwdRequest);
				return "front_end/customer/unlogined/memForgetPassword";
			}

			// 3.驗證成功，寄送Redis驗證碼
			String verificationCode = UUID.randomUUID().toString();
			long timeoutMinutes = 10; // 設定有效時間(分鐘)
			redisSvc.setVerificationCode(verificationCode, mem.getMemAcc(), timeoutMinutes);

			String mailTitle = "農作物與它們的產地：一般會員-重設密碼驗證信";
			String baseUrl = request.getScheme() + "://" + request.getServerName()
					+ ((request.getServerPort() == 80 || request.getServerPort() == 443) ? ""
							: ":" + request.getServerPort());
			String verifyUrl = "重設密碼請點擊下列連結：\n" + baseUrl + "/mem/resetPasswordPage?code=" + verificationCode + "\n\n"
					+ "此連結" + timeoutMinutes + "分鐘內有效，逾時請重新操作。";

			mailSvc.sendMail(mem.getMemEmail(), mailTitle, verifyUrl);

			redirectAttrs.addFlashAttribute("success", "成功發送驗證信");
			return "redirect:/mem/forgetPasswordPage"; // 重導到重設密碼頁面

		} catch (IllegalStateException e) {
			model.addAttribute("forgetPwdError", e.getMessage());
			model.addAttribute("forgetPwdRequest", forgetPwdRequest);
			return "front_end/customer/unlogined/memForgetPassword";
		}
	}

//	------------------重設密碼----------------

	@GetMapping("/resetPasswordPage")
	public String resetPasswordPage(@RequestParam("code") String code, ModelMap model, RedirectAttributes redirectAttrs,
			HttpSession session) {

		String memAcc = redisSvc.getMemEmailByCode(code);
		if (memAcc == null) {
			redirectAttrs.addFlashAttribute("fail", "驗證碼失效或不存在");
			return "redirect:/mem/forgetPasswordPage";
		}

		Mem memForResetPwd = memSvc.getOneByMemAccAndAuthProvider(memAcc, AuthProvider.LOCAL);
		if (memForResetPwd != null) {
			model.addAttribute("memForResetPwd", memForResetPwd);
			model.addAttribute("updatePasswordMem", new UpdatePasswordMem());
			session.setAttribute("memForResetPwd", memForResetPwd);

			session.setAttribute("code", code); // for重設密碼成功後 刪掉驗證碼
			return "front_end/customer/unlogined/memResetPassword";
		}
		redirectAttrs.addFlashAttribute("fail", "驗證碼失效或不存在");
		return "redirect:/mem/forgetPasswordPage";
	}

	@PostMapping("/resetPassword")
	public String resetPassword(
			@Validated(UpdatePasswordValidation.class) @ModelAttribute("updatePasswordMem") Mem resetPasswordMem,
			BindingResult result, ModelMap model, HttpSession session, RedirectAttributes redirectAttrs) {

		if (result.hasErrors()) {
			return "/front_end/customer/unlogined/memResetPassword";
		}

		Mem memForResetPwd = (Mem) session.getAttribute("memForResetPwd");
		if (memForResetPwd == null) {
			return "/front_end/customer/unlogined/memResetPassword";
		}

		memForResetPwd.setMemPwd(passwordEncoder.encode(resetPasswordMem.getMemPwd()));
		memSvc.updateMem(memForResetPwd);
		redirectAttrs.addFlashAttribute("success", "重設密碼成功");

		String code = (String) session.getAttribute("code");
		redisSvc.deleteCode(code);
		return "redirect:/mem/showMemRegLoginForm";
	}

	@PostMapping("/login")
	public String login(LoginRequest loginRequest, HttpSession session, ModelMap model) {
		String memAccLogin = loginRequest.getMemAccLogin();
		String memPwdLogin = loginRequest.getMemPwdLogin();

		// 1.基本欄位驗證
		if (memAccLogin == null || memAccLogin.trim().isEmpty()) {
			model.addAttribute("loginError", "請輸入帳號");
			model.addAttribute("loginRequest", loginRequest);
			model.addAttribute("mem", new Mem()); // 給註冊表單用
			model.addAttribute("activeTab", "login"); // 標記目前所在頁籤
			return "front_end/customer/unlogined/memRegLogin";
		}
		if (memPwdLogin == null || memPwdLogin.trim().isEmpty()) {
			model.addAttribute("loginError", "請輸入密碼");
			model.addAttribute("loginRequest", loginRequest);
			model.addAttribute("mem", new Mem()); // 給註冊表單用
			model.addAttribute("activeTab", "login"); // 標記目前所在頁籤
			return "front_end/customer/unlogined/memRegLogin";
		}

		// 2.呼叫service進行登入驗證
		try {
			Mem mem = memSvc.login(memAccLogin, memPwdLogin);

			if (mem == null) {
				model.addAttribute("loginError", "帳號或密碼錯誤");
				model.addAttribute("loginRequest", loginRequest);
				model.addAttribute("mem", new Mem()); // 給註冊表單用
				model.addAttribute("activeTab", "login"); // 標記目前所在頁籤
				return "front_end/customer/unlogined/memRegLogin";
			}
			
			
			// 3.登入成功，把會員資料存進session
			model.addAttribute("loggedInMember", mem);
			model.addAttribute("memId", mem.getMemId());
			session.setAttribute("memId", mem.getMemId());
			
			
			// *設定 Spring Security 的 SecurityContext
	        UsernamePasswordAuthenticationToken authentication = 
	            new UsernamePasswordAuthenticationToken(
	                mem, 
	                null, 
	                AuthorityUtils.createAuthorityList("ROLE_USER")
	            );
	        SecurityContextHolder.getContext().setAuthentication(authentication);
			
	        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
	        securityContext.setAuthentication(authentication);
	        SecurityContextHolder.setContext(securityContext);
	        
	        // *將 SecurityContext 儲存到 Session
	        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
	        
			// 4.登入成功後 重導至原本頁面
			String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
			
			if (redirectUrl != null) {
				session.removeAttribute("redirectAfterLogin");
				return "redirect:" + redirectUrl;
			}
			return "redirect:/mem/memArea";

		} catch (IllegalStateException e) {
			model.addAttribute("loginError", e.getMessage());
			model.addAttribute("loginRequest", loginRequest);
			model.addAttribute("mem", new Mem());
			model.addAttribute("activeTab", "login"); // 標記目前所在頁籤
			return "front_end/customer/unlogined/memRegLogin";
		}
	}

	@PostMapping("/logout")
	public String logout(HttpSession session, SessionStatus status) {
		// 1. 清掉 @SessionAttributes 管理的 model 屬性
		if (!status.isComplete()) {
			status.setComplete();
		}
		// 2. 清掉 HttpSession 屬性
		session.removeAttribute("loggedInMember");
		session.removeAttribute("memId");
		// 3. 重導到首頁
		return "redirect:/";
	}

}
