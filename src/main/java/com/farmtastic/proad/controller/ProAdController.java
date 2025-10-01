package com.farmtastic.proad.controller;

import java.io.IOException;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.proad.model.ProAdService;
import com.farmtastic.proad.model.ProAdVO;

import jakarta.servlet.http.HttpSession;


@Controller
@Validated
@RequestMapping("/")
public class ProAdController {

    @Autowired
    private ProAdService proAdService;
//  ******************************管理員功能**************************************
    @GetMapping("admin/proAd/list")
    public String list(@RequestParam(value = "proAdId", required = false) Integer proAdId,
    		           @RequestParam(value = "proAdRevStat", required = false) Integer proAdRevStat,
                       Model model) {
        model.addAttribute("listAllProAd", proAdService.getAll());
        model.addAttribute("proAdId", proAdId);        
        model.addAttribute("selectedType", "proAd");
        model.addAttribute("showReview", false); 
        
        //查單一
        if (proAdId != null) {
            if (proAdService.exists(proAdId)) {               
                model.addAttribute("proAdId", proAdId);        
                model.addAttribute("proAdVO", proAdService.getOneProAd(proAdId));
            } else {
                model.addAttribute("queryError", "查無 " + proAdId+" 號");  
            }
        }
        
      //查尚未審核
        if (proAdRevStat != null) {
            model.addAttribute("proAdRevStat", proAdRevStat);
            model.addAttribute("listAllProAd", proAdService.findByRevStat(proAdRevStat));
        }
        
        return "back_end/logined/ad/adminListAllProAd";
        }
    
    //將審核的頁面拉進來
    @GetMapping("admin/proAd/showReviewProAd")
    public String listReview(@RequestParam Integer proAdId, Model model){
        model.addAttribute("listAllProAd", proAdService.getAll());
        model.addAttribute("proAdId", proAdId);        
        model.addAttribute("proAdVO", proAdService.getOneProAd(proAdId));
        model.addAttribute("selectedType", "proAd");
        model.addAttribute("showReview", true);     
        return "back_end/logined/ad/adminListAllProAd";
    }
    
    //完成審核
    @PostMapping("admin/proAd/reviewProAd")
    public String reviewProAd(@RequestParam Integer proAdId,
                              @RequestParam String remark,
                              @RequestParam String action) {

        int status = "pass".equals(action) ? 4 : 3; // 4=待繳費, 3=不通過

        proAdService.updateStatus(proAdId, status, remark);

        return "redirect:/admin/proAd/list";
    }
    
    
  // 進入修改頁面
    @GetMapping("admin/proAd/showUpdateProAd")
    public String ShowUpdateProAd(@RequestParam Integer proAdId, Model model){
    	model.addAttribute("proAdVO", proAdService.getOneProAd(proAdId));
    	model.addAttribute("proAdId", proAdId);  
        return "back_end/logined/ad/adminUpdateProAd";
    }
    
    // 修改頁面提交
    @PostMapping("admin/proAd/updateProAd")
    public String updateProAd(@RequestParam Integer proAdId,
    						  @RequestParam("adImg") MultipartFile file,
    						  @RequestParam Integer proAdRevStat, 
    						  @RequestParam String proAdRevRemark, 
    						  @RequestParam Integer proAdLaunStat,
    						  @RequestParam Integer proAdFee, 
    						  @RequestParam Date proAdFeeEnd
    						  ) throws IOException {
    	
    	byte[] adImg = file.getBytes();
    	proAdService.updateProAd(proAdId, adImg ,proAdRevStat,proAdRevRemark,proAdLaunStat,proAdFee,proAdFeeEnd);
    	return "redirect:/admin/proAd/list";
    }
    
    
    
//    ******************************小農功能**************************************
    //小農申請商品廣告頁面顯示
    @GetMapping("farmer/proAd/applyAdView")
    public String show(ModelMap model) {
    	ProAdVO proAdVO = new ProAdVO();
        model.addAttribute("proAdVO", proAdVO); 
        return "front_end/farmer/logined/ad/farmerApplyProAd";
    }
    
    
    

    //小農申請商品廣告送出
    @PostMapping("farmer/proAd/applyProAd")
    public String farmerApplyProAd(
    		@ModelAttribute("proAdVO") ProAdVO proAdVO,
            BindingResult binding,
            @RequestParam("adImg") MultipartFile file,
            HttpSession session
    ) throws IOException {
    	

    	Integer fmemId = (Integer) session.getAttribute("fmemId");
        Fmem refFmemId = proAdService.getFmemRef(fmemId);
        proAdVO.setFmem(refFmemId);                                                
    	
        if (!file.isEmpty()) {
        	proAdVO.setProAdImg(file.getBytes()); // 把檔案 bytes 放回 VO
        }else {
            binding.rejectValue("proAdImg", "NotNull", "請選擇圖片");
        }
        
        if (proAdVO.getProAdStart() == null)
            binding.rejectValue("proAdStart", "NotNull", "請選擇廣告開始日期");
        
        
        if (binding.hasErrors()) {
            return "front_end/farmer/logined/ad/farmerApplyProAd";
        }
        proAdService.addProAd(proAdVO);           
        return "redirect:/farmer/proAd/list";
    }
    
    //小農查詢廣告申請列表
    @GetMapping("farmer/proAd/list")
    public String farmerListProAd(Model model) {
    	model.addAttribute("listProAd", proAdService.getAll());
    		
    return "front_end/farmer/logined/ad/farmerListProAd";
	}
    
//  ******************************消費者顯示前端功能**************************************
    @GetMapping("proAd/carousel")
    public String showAdCarousel(Model model) {
    	
    		
    return "front_end/farmer/logined/ad/farmerListProAd";
	}
    
    
    
    
    
    
//    *****************************相關功能**************************************
    //商品圖片顯示
    @GetMapping(value = "/img/{id}")
    @ResponseBody
    public byte[] img(@PathVariable Integer id) {
        return proAdService.getImgBytes(id);
    }
}
