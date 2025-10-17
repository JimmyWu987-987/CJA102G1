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
import com.farmtastic.memactcpn.model.MemActCpnServiceImp;
import com.farmtastic.member.model.Mem;
import com.farmtastic.proorder.model.ProOrderSevice;
import com.farmtastic.proorderitem.model.ProOrderItemService;
import com.farmtastic.reg.model.RegService;
import com.farmtastic.reg.model.RegVO;

import jakarta.servlet.http.HttpServletRequest;
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

	    List<RegVO> list = (regRevStat == null)
	            ? regService.getAll()                 // 沒帶參數：全部
	            : regService.findByRevStat(regRevStat); // 有帶參數：依狀態過濾
	    model.addAttribute("fmemList", fmemSvc.getAll());
	    model.addAttribute("listReg", list);
	    model.addAttribute("regRevStat", regRevStat); 
	    return "back_end/logined/reg/adminListAllReg";
	}

		//撥款成功後
		@PostMapping("regMoney")
		public String giveMonetToFmem(@RequestParam("regId") Integer regId,
									  @RequestParam("regStat") Integer regStat,
									  RedirectAttributes redirectAttributes ) {
		
		
		regService.updateRegStat(regId, regStat);
		redirectAttributes.addFlashAttribute("success","撥款成功");
		return "redirect:/admin/cashflow/reg/list";
	}
	
		@PostMapping("selectFmemReg")
		public String selectFmemProOrder(@RequestParam("fmemId") Integer fmemId,
		                                 ModelMap model,
		                                 HttpSession session) {

		    // 找小農
		    Fmem fmem = fmemSvc.getOneByFmemId(fmemId);
		    model.addAttribute("fmemName", fmem.getFmemName());
		    session.setAttribute("fmemId", fmem.getFmemId());

		    // 查該小農的訂單
		    List<RegVO> list = regService.getByFmemId(fmemId);
		    model.addAttribute("listReg", list);

		    // 下拉選單資料與選中的 fmemId
		    model.addAttribute("fmemList", fmemSvc.getAll());
		    model.addAttribute("selectedFmemId", fmemId);

		    return "back_end/logined/reg/adminListAllReg";
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
        
    	//取得會員折價卷(折價券的寫法)
        model.addAttribute("availableCoupons", memActCpnService.getValidCpnsByMember(mem.getMemId()));
    	
    	 // 帶到頁報名頁面
        model.addAttribute("actId", actId);
        model.addAttribute("sesId", sesId);
    	
    	// 同時帶 regVO 
    	regVO.setSesId(sesId);
    	regVO.setMemId(mem.getMemId());
    return "front_end/customer/logined/reg/memRegistrationAct";
    }
    
    
    
    
    
    
 // 消費者送出報名
    @PostMapping("mem/reg/actReg")
    public String memRegAct(@ModelAttribute("regVO") RegVO regVO,
                            BindingResult binding,
                            HttpSession session,
                            ModelMap model) {

        //-------------錯誤驗證------------------
        if (regVO.getRegName() == null || regVO.getRegName().isBlank())
            binding.rejectValue("regName", "blank", "請輸入姓名!");
        if (regVO.getRegMob() == null || !regVO.getRegMob().matches("^09\\d{8}$"))
            binding.rejectValue("regMob", "pattern", "手機號碼格式錯誤!");
        if (regVO.getRegMail() == null || !regVO.getRegMail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
            binding.rejectValue("regMail", "pattern", "Email 格式不正確!");

        if (binding.hasErrors()) {
            Mem mem = (Mem) session.getAttribute("loggedInMember");
            loadFormModel(model, regVO.getSesId(), mem.getMemId());
            return "front_end/customer/logined/reg/memRegistrationAct";
        }

        if (regVO.getRegPointDisc() == null) regVO.setRegPointDisc(0);

        // 回傳 regId
        RegVO saved = regService.addRegAndReturn(regVO);
        return "redirect:/mem/reg/pay?regId=" + saved.getRegId(); // 轉去付款
    }
    
    //錯誤時把資料回補
    private void loadFormModel(ModelMap model, Integer sesId, Integer memId) {
        model.addAttribute("sesInfo", regService.getSesInfoBySesId(sesId));
        model.addAttribute("currentPoints", regService.getMemberPoints(memId));
        model.addAttribute("availableCoupons", memActCpnService.getValidCpnsByMember(memId));
    }
    
    // 消費者付款
    @GetMapping("mem/reg/pay")
    public String showMemPayView(@RequestParam Integer regId,
                                 HttpServletRequest request) throws Exception {

        RegVO vo = regService.getOne(regId); // 取得報名資料

        // ===== 1. 組出 LINE Pay 請求內容 =====
        String dynamicUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String body = """
            {
              "amount": %d,
              "currency": "TWD",
              "orderId": "ACTREG-%d",
              "packages": [{
                "id": "PKG1",
                "amount": %d,
                "name": "活動報名費",
                "products": [{
                  "name": "活動報名",
                  "quantity": 1,
                  "price": %d
                }]
              }],
              "redirectUrls": {
                "confirmUrl": "%s/mem/reg/return?regId=%d",
                "cancelUrl": "%s/mem/reg/cancel"
              }
            }
            """.formatted(vo.getRegGrandTotal(), regId, vo.getRegGrandTotal(), vo.getRegGrandTotal(), dynamicUrl, regId, dynamicUrl);

        // ===== 2. 簽章 =====
        String base = "https://sandbox-api-pay.line.me";
        String path = "/v3/payments/request";
        String nonce = java.util.UUID.randomUUID().toString();
        String secret = "6e21d7668a02ac0e7f457fbf1bddd4e4";
        String sig = sign(secret + path + body + nonce);
        String channelId = "2008230869";

        // ===== 3. 呼叫 LINE Pay Request API =====
        var client = org.springframework.web.reactive.function.client.WebClient.create();
        java.util.Map<String, Object> r = client.post().uri(base + path)
            .header("X-LINE-ChannelId", channelId)
            .header("X-LINE-Authorization-Nonce", nonce)
            .header("X-LINE-Authorization", sig)
            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .retrieve().bodyToMono(java.util.Map.class).block();

        // ===== 4. 取得付款頁網址並導轉 =====
        String url = ((java.util.Map)((java.util.Map)r.get("info")).get("paymentUrl")).get("web").toString();
        return "redirect:" + url;
    }
    
    
    //活動折價券使用
    @Autowired
    private MemActCpnServiceImp memActCpnService;
    
 // 更新狀態
    @GetMapping("mem/reg/return")
    public String linePayReturn(@RequestParam String transactionId,
                                @RequestParam String orderId,
                                @RequestParam Integer regId,
                                @RequestParam(required = false) Integer cpnHolderDetailId,
                                RedirectAttributes redirectAttrs) {

        RegVO regVO = regService.getOne(regId);
        // 更新已付款
        regService.updatePayReg(regVO); 
        redirectAttrs.addFlashAttribute("successMsg", "報名成功！");
        return "redirect:/mem/reg/list";
    }

    // 簽章組成
    private String sign(String msg) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
        mac.init(new javax.crypto.spec.SecretKeySpec("6e21d7668a02ac0e7f457fbf1bddd4e4".getBytes(), "HmacSHA256"));
        return java.util.Base64.getEncoder().encodeToString(mac.doFinal(msg.getBytes()));
    }
    
   
    
    
    @GetMapping("act/review")
    public String showActReview(@ModelAttribute("regVO") RegVO regVO,
							   @RequestParam Integer actId,
							   ModelMap model) {
    	model.addAttribute("reviews", regService.getReviewsByActId(actId));
    return "front_end/customer/unlogined/act/actReview";
    }
    
}
