package com.farmtastic.reg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.fmember.model.FmemService;
import com.farmtastic.member.model.Mem;
import com.farmtastic.proad.model.ProAdVO;
import com.farmtastic.proorder.model.FmemOrderSummary;
import com.farmtastic.proorder.model.ProOrderSevice;
import com.farmtastic.proorderitem.model.ProOrderItemService;
import com.farmtastic.reg.model.RegService;
import com.farmtastic.reg.model.RegVO;

import jakarta.servlet.http.HttpSession;

@Controller
@Validated
@RequestMapping("/")
public class RegController {

	@Autowired
    private RegService regService;
	
	@Autowired
	ProOrderSevice proOrdSvc;
	@Autowired
	ProOrderItemService ProOrderItemSvc;
	@Autowired
	FmemService fmemSvc;
	
//  ******************************管理員功能**************************************
    //管理員查活動訂單全部
	@GetMapping("admin/cashflow/reg/list")
    public String list(Model model,
    				   @RequestParam(value = "regRevStat", required = false) Integer regRevStat) {
        model.addAttribute("listReg", regService.getAll());       
        
        
        //查尚未審核
        if (regRevStat != null) {
            model.addAttribute("regRevStat", regRevStat);
            model.addAttribute("listAllProAd",regService.findByRevStat(regRevStat));
        }
        
        
        
        return "back_end/logined/reg/adminListAllReg";
        }
	
	@PostMapping("regMoney")
	public String giveMonetToFmem(@RequestParam("regId") Integer regId,
								  @RequestParam("regStat") Integer regStat,
								  RedirectAttributes redirectAttributes ) {
		
		
		regService.updateRegStat(regId, regStat);
		redirectAttributes.addFlashAttribute("success","撥款成功");
		return "redirect:/admin/cashflow/reg/list";
	}
	
	// 搜尋該小農的全部訂單
		@PostMapping("selectFmemReg")
		public String selectFmemProOrder(@RequestParam("fmemId") Integer fmemId, 
				ModelMap model,
				HttpSession session) {
			
			// 取該小農的姓名
			Fmem fmem = fmemSvc.getOneByFmemId(fmemId);
			model.addAttribute("fmemName", fmem.getFmemName());
			
			// 進入詳細資料，按下回上一頁，保持列表為該小農的商品訂單列表
			session.setAttribute("fmemId", fmem.getFmemId());
			
			// 取該全部小農會員的id，給搜尋特定小農用。
			List<Fmem> fmemList = fmemSvc.getAll();
			model.addAttribute("fmemList", fmemList);
			
			
			return "/back_end/logined/cash_flow/index.html";
		}
	
	
	
	
	
//  ******************************小農功能**************************************
	//小農查詢廣告列表
    @GetMapping("fmem/reg/list")
    public String farmerListReg(Model model,HttpSession session) {
    	
    	Integer fmemId = (Integer) session.getAttribute("fmemId");
    	//顯示廣告列表
        model.addAttribute("listReg", regService.getByFmemId(fmemId));
        
        //顯示關聯
        model.addAttribute("extras", regService.getActAndSes(fmemId));
        return "front_end/farmer/logined/reg/farmerListReg";
        
        
	}
	
    //小農給予評價回覆
    @PostMapping("fmem/act/comm/reply")
    public String replyActComment(@RequestParam Integer regId,
                                  @RequestParam String actCommReply
                                  ) {
        regService.addActCommReply(regId, actCommReply);
        return "redirect:/fmem/reg/list";
    }

    
    
    
    
//  ******************************消費者功能**************************************
  //消費者查詢廣告列表
    @GetMapping("mem/reg/list")
    public String memListReg(Model model,HttpSession session) {
    	Mem mem = (Mem) session.getAttribute("loggedInMember");
    	
    	//消費者查詢活動訂單
        model.addAttribute("listReg", regService.getByMemId(mem.getMemId()));
      //顯示關聯
        model.addAttribute("extras", regService.getActAndSesByMemId(mem.getMemId()));
        
        return "front_end/customer/logined/reg/memListReg";
	}
    
  //消費者給予評價
    @PostMapping("mem/act/comm/rate")
    public String rateActComment(@RequestParam Integer regId,
						    	 @RequestParam Integer actRate,
						         @RequestParam String actComm
                                  ) {
        regService.addActRate(regId, actRate,actComm);
        return "redirect:/mem/reg/list";
    }
    
    
    
    // 消費者報名活動畫面
    @GetMapping("mem/reg/actReg")
    public String showMemRegAct(@ModelAttribute("regVO") RegVO regVO,
					    		@RequestParam Integer actId,
					            @RequestParam Integer sesId,
					    		HttpSession session,					
					    		ModelMap model) {
    	
    	Mem mem = (Mem) session.getAttribute("loggedInMember");
    	if(mem == null) {
    		return "redirect:/mem/showMemRegLoginForm";
    	}
    	
    	Integer currentPoints = regService.getMemberPoints(mem.getMemId());
    	
    	// 傳遞報名場次的資訊
        model.addAttribute("sesInfo", regService.getSesInfoBySesId(sesId));
        
    	// 傳遞會員目前的點數餘額
        model.addAttribute("currentPoints", currentPoints);
        
    	//取得會員折價卷
    	model.addAttribute("availableCoupons", regService.getCouponsByMemId(mem.getMemId()));
    	
    	 // 帶到頁報名頁面
        model.addAttribute("actId", actId);
        model.addAttribute("sesId", sesId);
    	
    	// 同時把 regVO 帶好
    	regVO.setSesId(sesId);
    	regVO.setMemId(mem.getMemId());
    return "front_end/customer/logined/reg/memRegistrationAct";
    }
    
    
    
    //消費者送出報名
    @PostMapping("mem/reg/actReg")
    public String memRegAct(@ModelAttribute("regVO") RegVO regVO,
    						BindingResult binding,
    						HttpSession session,
    						ModelMap model) {
    	
    	//-------------錯誤驗證------------------
        if (regVO.getRegName()==null || regVO.getRegName().isBlank())
            binding.rejectValue("regName","blank","請輸入姓名!");
        if (regVO.getRegMob()==null || !regVO.getRegMob().matches("^09\\d{8}$"))
            binding.rejectValue("regMob","pattern","手機號碼格式錯誤!");
        if (regVO.getRegMail()==null || !regVO.getRegMail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
            binding.rejectValue("regMail","pattern","Email 格式不正確!");

        if (binding.hasErrors()) {               
            Mem mem = (Mem) session.getAttribute("loggedInMember");
            loadFormModel(model, regVO.getSesId(), mem.getMemId());
            return "front_end/customer/logined/reg/memRegistrationAct";
        }
        
    	if (regVO.getRegPointDisc() == null) {
            regVO.setRegPointDisc(0); 
        }
    	regService.addReg(regVO);
    return "redirect:/mem/reg/list";
    }
    
    //錯誤時把資料回補
    private void loadFormModel(ModelMap model, Integer sesId, Integer memId) {
        model.addAttribute("sesInfo", regService.getSesInfoBySesId(sesId));
        model.addAttribute("currentPoints", regService.getMemberPoints(memId));
        model.addAttribute("availableCoupons", regService.getCouponsByMemId(memId));
    }
    
    
    @GetMapping("act/review")
    public String showActReview(@ModelAttribute("regVO") RegVO regVO,
							   @RequestParam Integer actId,
							   ModelMap model) {
    	model.addAttribute("reviews", regService.getReviewsByActId(actId));
    return "front_end/customer/unlogined/act/actReview";
    }
    
}
