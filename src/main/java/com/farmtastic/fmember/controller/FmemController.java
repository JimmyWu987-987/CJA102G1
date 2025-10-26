package com.farmtastic.fmember.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.act.model.Act;
import com.farmtastic.act.model.ActService;
import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.fmember.model.FmemService;
import com.farmtastic.fmember.model.ForgetPwdRequest;
import com.farmtastic.fmember.model.LoginRequest;
import com.farmtastic.fmember.model.TempPic;
import com.farmtastic.fmember.model.UpdatePasswordFmem;
import com.farmtastic.fmember.model.UpdateProfileFmem;
import com.farmtastic.fmember.model.UpdateStoreFmem;
import com.farmtastic.fmember.model.UpdateSupplementFmem;
import com.farmtastic.pro.model.Pro;
import com.farmtastic.pro.model.ProService;
import com.farmtastic.procom.model.ProComService;
import com.farmtastic.proimage.model.ProImage;
import com.farmtastic.proimage.model.ProImageService;
import com.farmtastic.redis.verification.MailService;
import com.farmtastic.redis.verification.RedisService;
import com.farmtastic.reg.model.RegService;
import com.farmtastic.style.model.Sty;
import com.farmtastic.style.model.StyService;
import com.farmtastic.util.OTPGenerator;
import com.farmtastic.validator.RegistrationValidation;
import com.farmtastic.validator.UpdatePasswordValidation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/fmem")
@SessionAttributes({"loggedInFmember", "fmemTest"})
public class FmemController{
	
	@Autowired
	FmemService fmemSvc;
	
	@Autowired
	StyService stySvc;
	
	@Autowired
	ProService proSvc;
	
	@Autowired
	ProComService proComSvc;
	
	@Autowired
	ProImageService proImageSvc;
	
	@Autowired
	ActService actSvc;
	
	@Autowired
	RegService regSvc;
	
	@Autowired
	RedisService redisSvc;
	
	@Autowired
	MailService mailSvc;
	
//	註冊頁面"超連結"
	@GetMapping("/showFmemRegLoginForm")
	public String showFmemRegLoginForm(ModelMap model) {
		model.addAttribute("fmem", new Fmem());
		model.addAttribute("loginRequest", new LoginRequest());
		return "front_end/farmer/unlogined/fmemRegLogin";
	}
	
//	登入後的小農首頁
	@GetMapping("/home")
	public String fmemHomeProd(HttpSession session, ModelMap model) {
		Fmem fmem = (Fmem) session.getAttribute("loggedInFmember");
		if(fmem != null) {
			if(fmem.getFmemPic() == null || 
			   fmem.getStorePic() == null || 
			   fmem.getLandPic() == null || 
			   fmem.getInsurPic() == null ||
			   fmem.getStoreName() == null ||
			   fmem.getStoreIntro() == null){
				
				return "/front_end/farmer/logined/homeNotOpen";
			}
			
			String StorePicBase64 = Base64.getEncoder().encodeToString(fmem.getStorePic());
			String fmemPicBase64 = Base64.getEncoder().encodeToString(fmem.getFmemPic());
			model.addAttribute("fmem", fmem);
			model.addAttribute("StorePicBase64", StorePicBase64);
			model.addAttribute("fmemPicBase64", fmemPicBase64);
			
			List<Pro> proList = proSvc.findByFmemId(fmem.getFmemId());
			for(Pro pro : proList) {
				Integer proId = pro.getProId();
				Optional<ProImage> proImage = proImageSvc.findFirstImageByProId(Long.valueOf(proId));
				
				if(proImage.isPresent()) {
					String proImageBase64 = Base64.getEncoder().encodeToString(proImage.orElse(null).getProImg());
					pro.setProImageBase64(proImageBase64);
				}
			}
			model.addAttribute("proList", proList);
		} else {
			return "front_end/farmer/unlogined/fmemRegLogin";
		}
		return "/front_end/farmer/logined/home";
	}
	
	@GetMapping("/homeAct")
	public String fmemHomeAct(HttpSession session, ModelMap model) {
		Fmem fmem = (Fmem) session.getAttribute("loggedInFmember");
		if(fmem != null) {
			String StorePicBase64 = Base64.getEncoder().encodeToString(fmem.getStorePic());
			String fmemPicBase64 = Base64.getEncoder().encodeToString(fmem.getFmemPic());
			model.addAttribute("fmem", fmem);
			model.addAttribute("StorePicBase64", StorePicBase64);
			model.addAttribute("fmemPicBase64", fmemPicBase64);
			
			List<Act> actList = actSvc.findByFmemId(fmem.getFmemId(), Sort.by(Sort.Direction.DESC, "actLaunUpd"));
			for(Act act : actList) {
				if(act.getActMainImg() != null) {
					act.setActMainImgBase64(Base64.getEncoder().encodeToString(act.getActMainImg()));
				}
			}
			model.addAttribute("actList", actList);
		} else {
			return "front_end/farmer/unlogined/fmemRegLogin";
		}
		return "/front_end/farmer/logined/homeAct";
	}

	
	@GetMapping("/fmemArea")
	public String fmemArea(HttpSession session, ModelMap model) {
		return "/front_end/farmer/logined/fmemArea";
	}
	
	
	@GetMapping("/fmemArea/updateProfilePage")
	public String updateProfilePage(
			HttpSession session,
			@ModelAttribute("loggedInFmember") Fmem loggedInFmember,
			ModelMap model) {

		if (loggedInFmember.getFmemPic() != null) {
			String fmemPicBase64 = Base64.getEncoder().encodeToString(loggedInFmember.getFmemPic());
			model.addAttribute("tempFmemPicBase64", fmemPicBase64);
		}
		if (loggedInFmember.getOrganicPic() != null) {
			String organicPicBase64 = Base64.getEncoder().encodeToString(loggedInFmember.getOrganicPic());
			model.addAttribute("tempOrganicPicBase64", organicPicBase64);
		}
		if (loggedInFmember.getLandPic() != null) {
			String landPicBase64 = Base64.getEncoder().encodeToString(loggedInFmember.getLandPic());
			model.addAttribute("tempLandPicBase64", landPicBase64);
		}
		if (loggedInFmember.getInsurPic() != null) {
			String insurPicBase64 = Base64.getEncoder().encodeToString(loggedInFmember.getInsurPic());
			model.addAttribute("tempInsurPicBase64", insurPicBase64);
		}
		
		UpdateProfileFmem updateProfileFmem = new UpdateProfileFmem();
		BeanUtils.copyProperties(loggedInFmember, updateProfileFmem);
		model.addAttribute("updateProfileFmem", updateProfileFmem);
		session.setAttribute("loggedInFmember", loggedInFmember); //*****
		return "/front_end/farmer/logined/fmemProfile/fmemUpdateProfile";
	}
	
//	修改個人資料 => 存進DB
	@PostMapping("/fmemArea/updateProfile")
	public String updateProfile(
			@Valid @ModelAttribute("updateProfileFmem") UpdateProfileFmem updateProfileFmem,
			BindingResult result, //一定要放在@Valid @ModelAttribute後面，不然如果有錯誤不會進controller
			ModelMap model,
			HttpSession session,
			RedirectAttributes redirectAttrs,
			HttpServletRequest request) throws IOException {
		
		// 抓使用者選擇的圖片
		MultipartFile fmemPicFile = updateProfileFmem.getFmemPic();
		MultipartFile organicPicFile = updateProfileFmem.getOrganicPic();
		MultipartFile landPicFile = updateProfileFmem.getLandPic();
		MultipartFile insurPicFile = updateProfileFmem.getInsurPic();
		
		//從session拿到loggedInFmember，把pic轉成base64放進暫存變數中tempXXXPicBase64
		Fmem loggedInFmember = (Fmem) session.getAttribute("loggedInFmember");
		if (loggedInFmember == null) {
			return "redirect:/fmem/showFmemRegLoginForm";
		}
		
		String tempFmemPicBase64 = null;
		String tempOrganicPicBase64 = null;
		String tempLandPicBase64 = null;
		String tempInsurPicBase64 = null;
		TempPic tempPic = (TempPic) session.getAttribute("tempPic");
		
//		第一次進來才做，把loggedInFmember的圖片存進暫存tempPic (byte[])
		if (tempPic == null) { //
			tempPic = new TempPic();
			tempPic.setFmemPic(loggedInFmember.getFmemPic());
			tempPic.setOrganicPic(loggedInFmember.getOrganicPic());
			tempPic.setLandPic(loggedInFmember.getLandPic());
			tempPic.setInsurPic(loggedInFmember.getInsurPic());
			session.setAttribute("tempPic", tempPic);
		}

//		給前端預覽用 (Base64)
//    	fmemPicFile沒選擇圖片且tempPic裡原本有圖，用舊的temp顯示在前端預覽
//    	fmemPicFile有選擇圖片且有錯誤的話，用舊的temp顯示在前端預覽** 且出現錯誤訊息紅字(後端驗證)
		if (tempPic.getFmemPic() != null) {
			tempFmemPicBase64 = Base64.getEncoder().encodeToString(tempPic.getFmemPic());			
			model.addAttribute("tempFmemPicBase64", tempFmemPicBase64); 
		}
		if (tempPic.getOrganicPic() != null) {
			tempOrganicPicBase64 = Base64.getEncoder().encodeToString(tempPic.getOrganicPic());
			model.addAttribute("tempOrganicPicBase64", tempOrganicPicBase64);
		}
		if (tempPic.getLandPic() != null) {
			tempLandPicBase64 = Base64.getEncoder().encodeToString(tempPic.getLandPic());
			model.addAttribute("tempLandPicBase64", tempLandPicBase64); 
		}
		if (tempPic.getInsurPic() != null) {
			tempInsurPicBase64 = Base64.getEncoder().encodeToString(tempPic.getInsurPic());
			model.addAttribute("tempInsurPicBase64", tempInsurPicBase64);
		}
		
//	    	fmemPicFile有選擇圖片且沒有錯誤的話，要把新的存進tempPic DTO，並用tempXXXBase64顯示在前端預覽
	    if (result.hasErrors()) {
	    	if(!result.hasFieldErrors("fmemPic")) {
	    		if(fmemPicFile != null && !fmemPicFile.isEmpty()) {
	    			tempFmemPicBase64 = Base64.getEncoder().encodeToString(fmemPicFile.getBytes());
	    			model.addAttribute("tempFmemPicBase64", tempFmemPicBase64); //前端預覽用Base64
	    			tempPic.setFmemPic(fmemPicFile.getBytes()); //後端暫存用byte[]
	    		}
	    	}
	    	if(!result.hasFieldErrors("organicPic")) {
	    		if(organicPicFile != null && !organicPicFile.isEmpty()) {
	    			tempOrganicPicBase64 = Base64.getEncoder().encodeToString(organicPicFile.getBytes());
	    			model.addAttribute("tempOrganicPicBase64", tempOrganicPicBase64); //前端預覽用Base64
	    			tempPic.setOrganicPic(organicPicFile.getBytes()); //後端暫存用byte[]
	    		}
	    	}
	    	if(!result.hasFieldErrors("landPic")) {
	    		if(landPicFile != null && !landPicFile.isEmpty()) {
	    			tempLandPicBase64 = Base64.getEncoder().encodeToString(landPicFile.getBytes());
	    			model.addAttribute("tempLandPicBase64", tempLandPicBase64); //前端預覽用Base64
	    			tempPic.setLandPic(landPicFile.getBytes()); //後端暫存用byte[]
	    		}
	    	}
	    	if(!result.hasFieldErrors("insurPic")) {
	    		if(insurPicFile != null && !insurPicFile.isEmpty()) {
	    			tempInsurPicBase64 = Base64.getEncoder().encodeToString(insurPicFile.getBytes());
	    			model.addAttribute("tempInsurPicBase64", tempInsurPicBase64); //前端預覽用Base64
	    			tempPic.setInsurPic(insurPicFile.getBytes()); //後端暫存用byte[]
	    		}
	    	}
	        return "/front_end/farmer/logined/fmemProfile/fmemUpdateProfile";
	    }

		BeanUtils.copyProperties(updateProfileFmem, loggedInFmember);
		
		byte[] tempFmemPic = tempPic.getFmemPic();
		byte[] tempOrganicPic = tempPic.getOrganicPic();
		byte[] tempLandPic = tempPic.getLandPic();
		byte[] tempInsurPic = tempPic.getInsurPic();
	
//		如果新update有圖 => 優先用update的
//		如果新update沒圖、但temp有圖 => 就用temp的
		if (fmemPicFile != null && !fmemPicFile.isEmpty()) {
    		loggedInFmember.setFmemPic(fmemPicFile.getBytes());
    	} else if ((fmemPicFile == null || fmemPicFile.isEmpty()) && tempFmemPic != null && tempFmemPic.length > 0) {
    		loggedInFmember.setFmemPic(tempFmemPic);
    	}
		if (organicPicFile != null && !organicPicFile.isEmpty()) {
    		loggedInFmember.setOrganicPic(organicPicFile.getBytes());
    	} else if ((organicPicFile == null || organicPicFile.isEmpty()) && tempOrganicPic != null && tempOrganicPic.length > 0) {
    		loggedInFmember.setOrganicPic(tempOrganicPic);
    	}
		if (landPicFile != null && !landPicFile.isEmpty()) {
    		loggedInFmember.setLandPic(landPicFile.getBytes());
    	} else if ((landPicFile == null || landPicFile.isEmpty()) && tempLandPic != null && tempLandPic.length > 0) {
    		loggedInFmember.setLandPic(tempLandPic);
    	}
		if (insurPicFile != null && !insurPicFile.isEmpty()) {
    		loggedInFmember.setInsurPic(insurPicFile.getBytes());
    	} else if ((insurPicFile == null || insurPicFile.isEmpty()) && tempInsurPic != null && tempInsurPic.length > 0) {
    		loggedInFmember.setInsurPic(tempInsurPic);
    	}

		fmemSvc.updateFmem(loggedInFmember);
		session.setAttribute("loggedInFmember", loggedInFmember); //index右上角顯示更新
		session.removeAttribute("tempPic"); //刪除session，不然登入其他會員也會存到舊的session資料
		redirectAttrs.addFlashAttribute("success", "修改資料成功");
		return "redirect:/fmem/fmemArea/updateProfilePage";
	}

	
	@GetMapping("/fmemArea/updatePasswordPage")
	public String updatePasswordPage(
			@ModelAttribute("loggedInFmember") Fmem loggedInFmember,
			ModelMap model) {
		
		UpdatePasswordFmem updatePasswordFmem = new UpdatePasswordFmem();
		BeanUtils.copyProperties(loggedInFmember, updatePasswordFmem);
		model.addAttribute("updatePasswordFmem", updatePasswordFmem);
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
	
	
	@GetMapping("/fmemArea/updateStorePage")
	public String updateStorePage(
			@ModelAttribute("loggedInFmember") Fmem loggedInFmember,
			ModelMap model) {
		
		if (loggedInFmember.getFmemPic() != null) {
			String tempFmemPicBase64 = Base64.getEncoder().encodeToString(loggedInFmember.getFmemPic());
			model.addAttribute("tempFmemPicBase64", tempFmemPicBase64);
		}
		if (loggedInFmember.getStorePic() != null) {
			String tempStorePicBase64 = Base64.getEncoder().encodeToString(loggedInFmember.getStorePic());
			model.addAttribute("tempStorePicBase64", tempStorePicBase64);
		}
		if(loggedInFmember.getSty().getStyPic() != null) {
			String tempStyPicBase64 = Base64.getEncoder().encodeToString(loggedInFmember.getSty().getStyPic());
			model.addAttribute("tempStyPicBase64", tempStyPicBase64);
		}
		
		UpdateStoreFmem updateStoreFmem = new UpdateStoreFmem();
		BeanUtils.copyProperties(loggedInFmember, updateStoreFmem);
		
		updateStoreFmem.setStyNo(loggedInFmember.getSty().getStyNo()); //sty
		
		Integer fmemId = loggedInFmember.getFmemId();
//		取得小農所有商品總分 + 評論數
		Integer totalProScore = proComSvc.countProComRateByFmemId(fmemId);
		Integer totalProCnt = proComSvc.countProComByFmemId(fmemId);
		
		if(totalProScore != null && totalProCnt != null) {
			loggedInFmember.setMktScore(totalProScore);
			loggedInFmember.setMktCnt(totalProCnt);
			
			if(totalProCnt > 0) {
				double avgMktScore = totalProScore * 1.0 / totalProCnt;
				model.addAttribute("avgMktScore", avgMktScore);
			}
		}
		
//		取得小農所有活動總分 + 評論數
		List<Integer> actScoreList = regSvc.getAllRatesByFmemId(fmemId);
		if(actScoreList != null && !actScoreList.isEmpty()) {
			Integer totalActScore = 0;
			for(Integer actScore : actScoreList) {
				totalActScore += actScore;
			}
			loggedInFmember.setActScore(totalActScore);
			loggedInFmember.setActCnt(actScoreList.size());
			
			if(totalActScore > 0) {
				double avgActScore = totalActScore * 1.0 / totalActScore;
				model.addAttribute("avgActScore", avgActScore);
			}
		}
		fmemSvc.updateFmem(loggedInFmember);
		model.addAttribute("updateStoreFmem", updateStoreFmem);
		return "/front_end/farmer/logined/fmemProfile/fmemUpdateStore";
	}
	
//	修改資料 => 存進DB
	@PostMapping("/fmemArea/updateStore")
	public String updateStore(
			@Valid @ModelAttribute("updateStoreFmem") UpdateStoreFmem updateStoreFmem,
			BindingResult result, //一定要放在@Valid @ModelAttribute後面，不然如果有錯誤不會進controller
			ModelMap model,
			HttpSession session,
			RedirectAttributes redirectAttrs,
			HttpServletRequest request) throws IOException {
		// 抓使用者選擇的圖片
		MultipartFile fmemPicFile = updateStoreFmem.getFmemPic();
		MultipartFile storePicFile = updateStoreFmem.getStorePic();
		
		Fmem loggedInFmember = (Fmem) session.getAttribute("loggedInFmember");
		if (loggedInFmember == null) {
			return "redirect:/fmem/showFmemRegLoginForm";
		}
		
		String tempFmemPicBase64 = null;
		String tempStorePicBase64 = null;

		TempPic tempPic = (TempPic) session.getAttribute("tempPic");
//		第一次進來才做，把loggedInFmember的圖片存進暫存tempPic (byte[])
		if (tempPic == null) { //
			tempPic = new TempPic();
			tempPic.setFmemPic(loggedInFmember.getFmemPic());
			tempPic.setStorePic(loggedInFmember.getStorePic());
			session.setAttribute("tempPic", tempPic);
		}
		
//		給前端預覽用 (Base64)
//    	fmemPicFile沒選擇圖片且tempPic裡原本有圖，用舊的temp顯示在前端預覽
//    	fmemPicFile有選擇圖片且有錯誤的話，用舊的temp顯示在前端預覽** 且出現錯誤訊息紅字(後端驗證)
		if (tempPic.getFmemPic() != null) {
			tempFmemPicBase64 = Base64.getEncoder().encodeToString(tempPic.getFmemPic());			
			model.addAttribute("tempFmemPicBase64", tempFmemPicBase64); 
		}
		if (tempPic.getStorePic() != null) {
			tempStorePicBase64 = Base64.getEncoder().encodeToString(tempPic.getStorePic());			
			model.addAttribute("tempStorePicBase64", tempStorePicBase64); 
		}
		
		// fmemPicFile有選擇圖片且沒有錯誤的話，要把新的存進tempPic DTO，並用tempXXXBase64顯示在前端預覽
	    if (result.hasErrors()) {
	    	if(!result.hasFieldErrors("fmemPic")) {
	    		if(fmemPicFile != null && !fmemPicFile.isEmpty()) {
	    			tempFmemPicBase64 = Base64.getEncoder().encodeToString(fmemPicFile.getBytes());
	    			model.addAttribute("tempFmemPicBase64", tempFmemPicBase64); //前端預覽用Base64
	    			tempPic.setFmemPic(fmemPicFile.getBytes()); //後端暫存用byte[]
	    		}
	    	}
	    	if(!result.hasFieldErrors("storePic")) {
	    		if(storePicFile != null && !storePicFile.isEmpty()) {
	    			tempStorePicBase64 = Base64.getEncoder().encodeToString(storePicFile.getBytes());
	    			model.addAttribute("tempStorePicBase64", tempStorePicBase64); //前端預覽用Base64
	    			tempPic.setStorePic(storePicFile.getBytes()); //後端暫存用byte[]
	    		}
	    	}
	        return "/front_end/farmer/logined/fmemProfile/fmemUpdateStore";
	    }
	    BeanUtils.copyProperties(updateStoreFmem, loggedInFmember);
		
		// 樣式更新
		if(updateStoreFmem.getStyNo() != null) {
			Sty sty = stySvc.getOneByStyNo(updateStoreFmem.getStyNo());
			loggedInFmember.setSty(sty);
		}
		
		byte[] tempFmemPic = tempPic.getFmemPic();
		byte[] tempStorePic = tempPic.getStorePic();

//		如果新update有圖 => 優先用update的
//		如果新update沒圖、但temp有圖 => 就用temp的
		if (fmemPicFile != null && !fmemPicFile.isEmpty()) {
			loggedInFmember.setFmemPic(fmemPicFile.getBytes());
		} else if ((fmemPicFile == null || fmemPicFile.isEmpty()) && tempFmemPic != null && tempFmemPic.length > 0) {
			loggedInFmember.setFmemPic(tempFmemPic);
		}
		if (storePicFile != null && !storePicFile.isEmpty()) {
    		loggedInFmember.setStorePic(storePicFile.getBytes());
    	} else if ((storePicFile == null || storePicFile.isEmpty()) && tempStorePic != null && tempStorePic.length > 0) {
    		loggedInFmember.setStorePic(tempStorePic);
    	}
		fmemSvc.updateFmem(loggedInFmember);
	
		if (fmemPicFile != null && !fmemPicFile.isEmpty()) {
			loggedInFmember.setFmemPic(fmemPicFile.getBytes());
		}
		if (storePicFile != null && !storePicFile.isEmpty()) {
			loggedInFmember.setStorePic(storePicFile.getBytes());
		}
		
		// 商店資料上傳完整就會"啟用"帳號
		if(loggedInFmember.getAccStatus() == 1) {
			if(loggedInFmember.getStoreName() != null
					&& loggedInFmember.getStoreIntro() != null
					&& loggedInFmember.getFmemPic() != null
					&& loggedInFmember.getStorePic() != null) {
				loggedInFmember.setAccStatus((byte) 2);
				fmemSvc.updateFmem(loggedInFmember);
				redirectAttrs.addFlashAttribute("accStatus", "已啟用");

				// 寄信通知小農：商店已啟用
				String baseUrl = request.getScheme() + "://" + request.getServerName() + 
						 ( (request.getServerPort() == 80 || request.getServerPort() == 443) ? "" : ":" + request.getServerPort() );
				String mailTitle = "農作物與它們的產地：小農商店啟用通知信";
				String verifyUrl = "恭喜您已完成開店流程！可開始上架及販售商品\n" 
									+ "首頁連結：" + baseUrl + "\n"
									+ "登入連結：" + baseUrl + "/fmem/showFmemRegLoginForm\n";
				mailSvc.sendMail(loggedInFmember.getFmemEmail(), mailTitle, verifyUrl);
			}
		}
		session.setAttribute("loggedInFmember", loggedInFmember); //index右上角顯示更新
		session.removeAttribute("tempPic"); //刪除session，不然登入其他會員也會存到舊的session資料
		redirectAttrs.addFlashAttribute("success", "修改資料成功");
		return "redirect:/fmem/fmemArea/updateStorePage";
	}
	
	
//	送出註冊"表單"
	@PostMapping("/register")
	public String register(
			@Validated(RegistrationValidation.class) @ModelAttribute("fmem") Fmem fmem, 
			BindingResult result, 
			ModelMap model, 
			RedirectAttributes redirectAttrs,
			HttpSession session) throws IOException {
	
		// 驗證帳號、手機、身分證不能跟別人重複
		String fmemAcc = fmem.getFmemAcc();
		String fmemMobile = fmem.getFmemMobile();
		String fId = fmem.getFId();
		if (fmemSvc.existsByFmemAcc(fmemAcc)) {
			result.rejectValue("fmemAcc", null, "此帳號已有人註冊過");
		}
		if (fmemSvc.existsByFmemMobile(fmemMobile)) {
			result.rejectValue("fmemMobile", null, "此手機已有人註冊過");
		}
		if (fmemSvc.existsByFId(fId)) {
			result.rejectValue("fId", null, "此身分證已有人註冊過");
		}

		// 取得上傳的圖片
		MultipartFile landPicFile = fmem.getLandPicFile();
		MultipartFile insurPicFile = fmem.getInsurPicFile();
		
		// 取得 tempPic
		TempPic tempPic = (TempPic) session.getAttribute("tempPic");
		if(tempPic == null) {
			tempPic = new TempPic();
			session.setAttribute("tempPic", tempPic);
		}
		
		// 先處理新上傳的圖片
	    // 如果這次有上傳新圖且沒錯誤 > 更新 tempPic
	    // 如果這次沒上傳或有錯誤 > 用舊的 tempPic
		if(landPicFile != null && !landPicFile.isEmpty() && !result.hasFieldErrors("landPicFile")) {
			tempPic.setLandPic(landPicFile.getBytes());
		}
		if(insurPicFile != null && !insurPicFile.isEmpty() && !result.hasFieldErrors("insurPicFile")) {
			tempPic.setInsurPic(insurPicFile.getBytes());
		}
		
		String tempLandPicBase64 = null;
		String tempInsurPicBase64 = null;
		
		if(tempPic.getLandPic() != null && tempPic.getLandPic().length != 0) {
			tempLandPicBase64 = Base64.getEncoder().encodeToString(tempPic.getLandPic());
			model.addAttribute("tempLandPicBase64", tempLandPicBase64);
		} else if(!result.hasFieldErrors("landPicFile")) {
			result.rejectValue("landPicFile", null, "農地證明圖片請勿空白");
		}
		
		if(tempPic.getInsurPic() != null && tempPic.getInsurPic().length != 0) {
			tempInsurPicBase64 = Base64.getEncoder().encodeToString(tempPic.getInsurPic());
			model.addAttribute("tempInsurPicBase64", tempInsurPicBase64);
		} else if(!result.hasFieldErrors("insurPicFile")) {
			result.rejectValue("insurPicFile", null, "保險證明圖片請勿空白");
		}
		
		if(result.hasErrors()) {
			session.setAttribute("tempPic", tempPic);  // 更新session裡的資料
			model.addAttribute("loginRequest", new LoginRequest()); // 給login用
			model.addAttribute("activeTab", "register"); //標記目前所在頁籤
			return "front_end/farmer/unlogined/fmemRegLogin";
		}
		
		// 沒有錯誤，準備存入資料庫
		byte[] tempLandPic = tempPic.getLandPic();
		byte[] tempInsurPic = tempPic.getInsurPic();
		
		if (landPicFile != null && !landPicFile.isEmpty()) {
			fmem.setLandPic(landPicFile.getBytes());
		} else if (tempLandPic != null && tempLandPic.length > 0) {
			fmem.setLandPic(tempLandPic);
		}
		
		if (insurPicFile != null && !insurPicFile.isEmpty()) {
			fmem.setInsurPic(insurPicFile.getBytes());
		} else if (tempInsurPic != null && tempInsurPic.length > 0) {
			fmem.setInsurPic(tempInsurPic);
		}
		
		fmemSvc.addFmem(fmem);
		session.removeAttribute("tempPic");
		redirectAttrs.addFlashAttribute("success", "小農會員註冊成功");
		return "redirect:/"; //要重導到小農首頁
	}
	
	
//	------------------忘記密碼----------------
	@GetMapping("/forgetPasswordPage")
	public String forgetPasswordPage(ModelMap model) {
		model.addAttribute("forgetPwdRequest", new ForgetPwdRequest());
		return "front_end/farmer/unlogined/fmemForgetPassword";
	}

	@PostMapping("/forgetPassword")
	public String forgetPassword(
			HttpServletRequest request,
			ForgetPwdRequest forgetPwdRequest, 
			HttpSession session, 
			ModelMap model,
			RedirectAttributes redirectAttrs) {
		String fmemMobileForgetPwd = forgetPwdRequest.getFmemMobileForgetPwd();
		String fmemEmailForgetPwd = forgetPwdRequest.getFmemEmailForgetPwd();
		
		// 1.基本欄位驗證 
		if(fmemMobileForgetPwd == null || fmemMobileForgetPwd.trim().isEmpty()) {
			model.addAttribute("forgetPwdError", "請輸入手機");
			model.addAttribute("forgetPwdRequest", forgetPwdRequest);
			return "front_end/farmer/unlogined/fmemForgetPassword";
		}
		if(fmemEmailForgetPwd == null || fmemEmailForgetPwd.trim().isEmpty()) {
			model.addAttribute("forgetPwdError", "請輸入信箱");
			model.addAttribute("forgetPwdRequest", forgetPwdRequest);
			return "front_end/farmer/unlogined/fmemForgetPassword";
		}
		
		// 2.呼叫service進行驗證
		try {
			Fmem fmem = fmemSvc.forgetPassword(fmemMobileForgetPwd, fmemEmailForgetPwd);
			if(fmem == null) {
				model.addAttribute("forgetPwdError", "查無此帳號");
				model.addAttribute("forgetPwdRequest", forgetPwdRequest);
				return "front_end/farmer/unlogined/fmemForgetPassword";
			}
			
			// 3.驗證成功，寄送Redis驗證碼
			String verificationCode = UUID.randomUUID().toString(); 
			long timeoutMinutes = 10;  //設定有效時間(分鐘)
			redisSvc.setVerificationCode(verificationCode, fmem.getFmemAcc(), timeoutMinutes);
			
			String baseUrl = request.getScheme() + "://" + request.getServerName() + 
					 ( (request.getServerPort() == 80 || request.getServerPort() == 443) ? "" : ":" + request.getServerPort() );
			String mailTitle = "農作物與它們的產地：一般會員-重設密碼驗證信";
			String verifyUrl = "重設密碼請點擊下列連結：\n"
			        + baseUrl + "/fmem/resetPasswordPage?code=" + verificationCode + "\n\n"
			        + "此連結" + timeoutMinutes +"分鐘內有效，逾時請重新操作。";
			
			mailSvc.sendMail(fmem.getFmemEmail(), mailTitle, verifyUrl);
			
			redirectAttrs.addFlashAttribute("success", "成功發送驗證信");
			return "redirect:/fmem/forgetPasswordPage";  //重導到重設密碼頁面
			
		} catch (IllegalStateException e) {
			model.addAttribute("forgetPwdError", e.getMessage());
			model.addAttribute("forgetPwdRequest", forgetPwdRequest);
			return "front_end/farmer/unlogined/fmemForgetPassword";
		}
	}
	
	
//	------------------重設密碼----------------
	@GetMapping("/resetPasswordPage")
	public String resetPasswordPage(
			@RequestParam("code") String code,
			ModelMap model,
			RedirectAttributes redirectAttrs,
			HttpSession session) {
		
		String fmemAcc = redisSvc.getMemAccByCode(code);
		if (fmemAcc == null) {
			redirectAttrs.addFlashAttribute("fail", "驗證碼失效或不存在");
			return "redirect:/fmem/forgetPasswordPage";
		}
		
		Fmem fmemForResetPwd = fmemSvc.getOneByFmemAcc(fmemAcc);
		if (fmemForResetPwd != null) {
			model.addAttribute("fmemForResetPwd", fmemForResetPwd);
			model.addAttribute("updatePasswordFmem", new UpdatePasswordFmem());
			session.setAttribute("fmemForResetPwd", fmemForResetPwd);

			session.setAttribute("code", code);  // for重設密碼成功後 刪掉驗證碼
			return "front_end/farmer/unlogined/fmemResetPassword";
		}
		redirectAttrs.addFlashAttribute("fail", "驗證碼失效或不存在");
		return "redirect:/fmem/forgetPasswordPage";
	}
	
	@PostMapping("/resetPassword")
	public String resetPassword(
			@Validated(UpdatePasswordValidation.class) @ModelAttribute("updatePasswordFmem") Fmem resetPasswordFmem,
			BindingResult result,
			ModelMap model,
			HttpSession session,
			RedirectAttributes redirectAttrs) {
		
		if (result.hasErrors()) {
			return "/front_end/farmer/unlogined/fmemResetPassword";
		}
		Fmem fmemForResetPwd = (Fmem) session.getAttribute("fmemForResetPwd");
		if (fmemForResetPwd == null) {
			return "/front_end/farmer/unlogined/fmemResetPassword";
		}
		fmemForResetPwd.setFmemPwd(resetPasswordFmem.getFmemPwd());
		fmemSvc.updateFmem(fmemForResetPwd);
		redirectAttrs.addFlashAttribute("success", "重設密碼成功");
		
		String code = (String) session.getAttribute("code");
		redisSvc.deleteCode(code);
		return "redirect:/fmem/showFmemRegLoginForm";
	}
	
	
//	小農補件 (借用忘記密碼DTO)
	@GetMapping("/supplementIdentityCheckPage")
	public String supplementIdentityCheckPage(ModelMap model) {
		model.addAttribute("forgetPwdRequest", new ForgetPwdRequest());
		return "front_end/farmer/unlogined/fmemSupplementIdentityCheck";
	}

	@PostMapping("/requestSupplement")
	public String requestSupplement(
			HttpServletRequest request,
			ForgetPwdRequest forgetPwdRequest, 
			HttpSession session, 
			ModelMap model,
			RedirectAttributes redirectAttrs) {
		String fmemMobileForgetPwd = forgetPwdRequest.getFmemMobileForgetPwd();
		String fmemEmailForgetPwd = forgetPwdRequest.getFmemEmailForgetPwd();
		
		// 1.基本欄位驗證 
		if(fmemMobileForgetPwd == null || fmemMobileForgetPwd.trim().isEmpty()) {
			model.addAttribute("forgetPwdError", "請輸入手機");
			model.addAttribute("forgetPwdRequest", forgetPwdRequest);
			return "front_end/farmer/unlogined/fmemSupplementIdentityCheck";
		}
		if(fmemEmailForgetPwd == null || fmemEmailForgetPwd.trim().isEmpty()) {
			model.addAttribute("forgetPwdError", "請輸入信箱");
			model.addAttribute("forgetPwdRequest", forgetPwdRequest);
			return "front_end/farmer/unlogined/fmemSupplementIdentityCheck";
		}
		
		// 2.呼叫service進行驗證
		try {
			Fmem verifiedFmem = fmemSvc.checkSupplementIdentity(fmemMobileForgetPwd, fmemEmailForgetPwd);
			if(verifiedFmem == null) {
				model.addAttribute("forgetPwdError", "此帳號不符合補件資格");
				model.addAttribute("forgetPwdRequest", forgetPwdRequest);
				return "front_end/farmer/unlogined/fmemSupplementIdentityCheck";
			}
			
			// 3.驗證成功，把fmem傳給下一個頁面
			session.setAttribute("verifiedFmem", verifiedFmem);
			return "redirect:/fmem/fmemSupplementFormPage";  //重導到重設密碼頁面
			
		} catch (IllegalStateException e) {
			model.addAttribute("forgetPwdError", e.getMessage());
			model.addAttribute("forgetPwdRequest", forgetPwdRequest);
			return "front_end/farmer/unlogined/fmemSupplementIdentityCheck";
		}
	}
	
	
	@GetMapping("/fmemSupplementFormPage")
	public String fmemSupplementFormPage(
			ModelMap model,
			HttpSession session) throws IOException {
		
		Fmem verifiedFmem = (Fmem) session.getAttribute("verifiedFmem");
		if (verifiedFmem == null) {
	        // 可能導向登入頁面或提示錯誤
	        return "front_end/farmer/unlogined/fmemSupplementIdentityCheck";
	    }
		
		if (verifiedFmem.getLandPic() != null) {
			String landPicBase64 = Base64.getEncoder().encodeToString(verifiedFmem.getLandPic());
			model.addAttribute("tempLandPicBase64", landPicBase64);
		}
		if (verifiedFmem.getInsurPic() != null) {
			String insurPicBase64 = Base64.getEncoder().encodeToString(verifiedFmem.getInsurPic());
			model.addAttribute("tempInsurPicBase64", insurPicBase64);
		}
		
		UpdateSupplementFmem updateSupplementFmem = new UpdateSupplementFmem();
		BeanUtils.copyProperties(verifiedFmem, updateSupplementFmem);
		model.addAttribute("updateSupplementFmem", updateSupplementFmem);
		model.addAttribute("verifiedFmem", verifiedFmem);
		return "front_end/farmer/unlogined/fmemSupplementForm";
	}
	
	
	@PostMapping("/submitSupplement")
	public String submitSupplement(
			ModelMap model,
			HttpSession session,
			@Valid @ModelAttribute("updateSupplementFmem") UpdateSupplementFmem updateSupplementFmem,
			BindingResult result,
			RedirectAttributes redirectAttrs) throws IOException {
		
		Fmem verifiedFmem = (Fmem) session.getAttribute("verifiedFmem");
		model.addAttribute("verifiedFmem", verifiedFmem);
		if (verifiedFmem == null) {
	        // 可能導向登入頁面或提示錯誤
	        return "front_end/farmer/unlogined/fmemSupplementIdentityCheck";
	    }
		
//		處理圖片
		MultipartFile landPicFile = updateSupplementFmem.getLandPic();
		MultipartFile insurPicFile = updateSupplementFmem.getInsurPic();
		
		String tempLandPicBase64 = null;
		String tempInsurPicBase64 = null;
		TempPic tempPic = (TempPic) session.getAttribute("tempPic");
		
//		第一次進來才做，把loggedInFmember的圖片存進暫存tempPic (byte[])
		if (tempPic == null) { //
			tempPic = new TempPic();
			tempPic.setLandPic(verifiedFmem.getLandPic());
			tempPic.setInsurPic(verifiedFmem.getInsurPic());
			session.setAttribute("tempPic", tempPic);
		}
			
	//	fmemPicFile沒選擇圖片且tempPic裡原本有圖，用舊的temp顯示在前端預覽
	//	fmemPicFile有選擇圖片且有錯誤的話，用舊的temp顯示在前端預覽** 且出現錯誤訊息紅字(後端驗證)
		if (tempPic.getLandPic() != null) {
			tempLandPicBase64 = Base64.getEncoder().encodeToString(tempPic.getLandPic());
			model.addAttribute("tempLandPicBase64", tempLandPicBase64); 
		}
		if (tempPic.getInsurPic() != null) {
			tempInsurPicBase64 = Base64.getEncoder().encodeToString(tempPic.getInsurPic());
			model.addAttribute("tempInsurPicBase64", tempInsurPicBase64);
		}
		
//    	fmemPicFile有選擇圖片且沒有錯誤的話，要把新的存進tempPic DTO，並用tempXXXBase64顯示在前端預覽
    	if (result.hasErrors()) {
			if(!result.hasFieldErrors("landPic")) {
				if(landPicFile != null && !landPicFile.isEmpty()) {
					tempLandPicBase64 = Base64.getEncoder().encodeToString(landPicFile.getBytes());
					model.addAttribute("tempLandPicBase64", tempLandPicBase64); //前端預覽用Base64
					tempPic.setLandPic(landPicFile.getBytes()); //後端暫存用byte[]
				}
			}
			if(!result.hasFieldErrors("insurPic")) {
				if(insurPicFile != null && !insurPicFile.isEmpty()) {
					tempInsurPicBase64 = Base64.getEncoder().encodeToString(insurPicFile.getBytes());
					model.addAttribute("tempInsurPicBase64", tempInsurPicBase64); //前端預覽用Base64
					tempPic.setInsurPic(insurPicFile.getBytes()); //後端暫存用byte[]
				}
			}
		    return "front_end/farmer/unlogined/fmemSupplementForm";
		}
    	
//    	資料正確，開始存進資料庫
		BeanUtils.copyProperties(updateSupplementFmem, verifiedFmem);
		byte[] tempLandPic = tempPic.getLandPic();
		byte[] tempInsurPic = tempPic.getInsurPic();
	
//		如果新update有圖 => 優先用update的
//		如果新update沒圖、但temp有圖 => 就用temp的
		if (landPicFile != null && !landPicFile.isEmpty()) {
			verifiedFmem.setLandPic(landPicFile.getBytes());
    	} else if ((landPicFile == null || landPicFile.isEmpty()) && tempLandPic != null && tempLandPic.length > 0) {
    		verifiedFmem.setLandPic(tempLandPic);
    	}
		if (insurPicFile != null && !insurPicFile.isEmpty()) {
			verifiedFmem.setInsurPic(insurPicFile.getBytes());
    	} else if ((insurPicFile == null || insurPicFile.isEmpty()) && tempInsurPic != null && tempInsurPic.length > 0) {
    		verifiedFmem.setInsurPic(tempInsurPic);
    	}
    	
		verifiedFmem.setAccStatus((byte) 0); //進入待審核
    	fmemSvc.updateFmem(verifiedFmem);
    	session.removeAttribute("tempPic"); //刪除session，不然登入其他會員也會存到舊的session資料
    	session.removeAttribute("verifiedFmem");
		redirectAttrs.addFlashAttribute("success", "補件上傳成功");
    	return "redirect:/";
	}
	
	
	@PostMapping("/login")
	public String login(
			LoginRequest loginRequest, 
			BindingResult result,
			HttpSession session, 
			ModelMap model) {
		
		String fmemAccLogin = loginRequest.getFmemAccLogin();
		String fmemPwdLogin = loginRequest.getFmemPwdLogin();
		model.addAttribute("fmem", new Fmem());
		
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
			Fmem fmem = fmemSvc.getOneByFmemAcc(fmemAccLogin);
			
			// 先檢查帳號是否存在
			if(fmem == null) {
				model.addAttribute("loginError", "帳號或密碼錯誤"); //查無此帳號
				model.addAttribute("loginRequest", loginRequest);
				model.addAttribute("fmem", new Fmem());
				model.addAttribute("activeTab", "login");  //標記目前所在頁籤
				return "front_end/farmer/unlogined/fmemRegLogin";
			}
			
			// 如果帳號已被鎖定
			if(fmem.getLockTime() != null) {
				long diff = Duration.between(fmem.getLockTime().toInstant(), Instant.now()).toMinutes(); //無條件捨去小數部分
				if(diff < 10) {
					model.addAttribute("loginError", "帳號已鎖定，請稍後再試（剩餘 " + (10 - diff) + " 分鐘）");
					return "front_end/farmer/unlogined/fmemRegLogin";
				} else { //超過10分鐘 自動解鎖
					fmem.setLockTime(null);
					fmem.setFailAttempts(0);
					fmemSvc.updateFmem(fmem);
				}
			}
			
			// 檢查密碼是否正確
			if(!fmem.getFmemPwd().equals(fmemPwdLogin)) {
				int newAttempts = fmem.getFailAttempts() + 1;
				fmem.setFailAttempts(newAttempts);
				
				if(newAttempts >= 5) {
					fmem.setLockTime(new Timestamp(System.currentTimeMillis()));
				}
				fmemSvc.updateFmem(fmem);
				model.addAttribute("loginError", "帳號或密碼錯誤");
				return "front_end/farmer/unlogined/fmemRegLogin";
			}
			
			// 帳號狀態檢查
			if((fmem.getAccStatus() != 2) && (fmem.getAccStatus() != 1)) {
				model.addAttribute("loginError", "帳號尚未通過審核或已被停權");
				return "front_end/farmer/unlogined/fmemRegLogin";
			}
			
			// 3.登入成功，把會員資料存進session
			model.addAttribute("loggedInFmember", fmem); //@SessionAttributes會自動幫我存進session
			model.addAttribute("fmemId", fmem.getFmemId());
			session.setAttribute("fmemId", fmem.getFmemId());
			// 發送OTP驗證碼
			String verificationCode = OTPGenerator.generateOTP();
			long timeoutMinutes = 5; //設定有效時間(分鐘)
			redisSvc.setVerificationCode(verificationCode, fmem.getFmemAcc(), timeoutMinutes);
			
			String mailTitle = "農作物與它們的產地：小農會員-登入驗證碼";
			String mailContent = "以下是您的登入驗證碼：" + verificationCode + "\n"
			        		   + "此驗證碼" + timeoutMinutes +"分鐘內有效，逾時請重新操作。";
			mailSvc.sendMail(fmem.getFmemEmail(), mailTitle, mailContent);
						
			// 4.登入成功後 重導至OTP驗證***
//			return "redirect:/fmem/home";
			return "redirect:/fmem/loginVerifyPage";
		} catch (IllegalStateException e) {
			model.addAttribute("loginError", e.getMessage());
			model.addAttribute("loginRequest", loginRequest);
			model.addAttribute("fmem", new Fmem());
			model.addAttribute("activeTab", "login");  //標記目前所在頁籤
			return "front_end/farmer/unlogined/fmemRegLogin";
		}
	}
	
	@GetMapping("/loginVerifyPage")
	public String loginVerifyPage(ModelMap model) {
		return "front_end/farmer/unlogined/fmemLoginVerifyOTP";
	}
	
	@PostMapping("/loginVerify")
	public String loginVerify(
			@RequestParam("otpCode") String otpCode, 
			ModelMap model,
			RedirectAttributes redirectAttrs) {
		
		String fmemAcc = redisSvc.getMemAccByCode(otpCode);
		if (fmemAcc == null) {
			redirectAttrs.addFlashAttribute("fail", "OTP驗證失敗");
			return "redirect:/fmem/showFmemRegLoginForm"; //驗證失敗回去登入頁
		}
		return "redirect:/fmem/home";
	}	
	
	@PostMapping("/logout")
	public String logout(HttpSession session, SessionStatus status) {
		if(!status.isComplete()) {
			status.setComplete();
		}
		session.removeAttribute("loggedInMember");
		session.removeAttribute("fmemId");
		return "redirect:/fmem/showFmemRegLoginForm";
	}
	
}
