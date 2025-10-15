package com.farmtastic.actad.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.farmtastic.act.model.Act;
import com.farmtastic.actad.model.ActAdService;
import com.farmtastic.actad.model.ActAdVO;
import com.farmtastic.fmember.model.Fmem;

import jakarta.servlet.http.HttpSession;

@Controller
@Validated
@RequestMapping("/")
public class ActAdController {

    @Autowired
    private ActAdService actAdService;
//  ******************************管理員功能**************************************
    @GetMapping("admin/actAd/list")
    public String list(@RequestParam(value = "actAdId", required = false) Integer actAdId,
    		           @RequestParam(value = "actAdRevStat", required = false) Integer actAdRevStat,
                       Model model) {
        model.addAttribute("listAllActAd", actAdService.getAll());
        model.addAttribute("actAdId", actAdId);        
        model.addAttribute("selectedType", "actAd");
        model.addAttribute("showReview", false); 
        
        //查單一
        if (actAdId != null) {
            if (actAdService.exists(actAdId)) {               
                model.addAttribute("actAdId", actAdId);        
                model.addAttribute("actAdVO", actAdService.getOneActAd(actAdId));
            } else {
                model.addAttribute("queryError", "查無 " + actAdId+" 號");  
            }
        }
        
      //查尚未審核
        if (actAdRevStat != null) {
            model.addAttribute("actAdRevStat", actAdRevStat);
            model.addAttribute("listAllActAd", actAdService.findByRevStat(actAdRevStat));
        }
        
        return "back_end/logined/ad/adminListAllActAd";
        }
    
    //將審核的頁面拉進來
    @GetMapping("admin/actAd/showReviewActAd")
    public String listReview(@RequestParam Integer actAdId, Model model){
        model.addAttribute("listAllActAd", actAdService.getAll());
        model.addAttribute("actAdId", actAdId);        
        model.addAttribute("actAdVO", actAdService.getOneActAd(actAdId));
        model.addAttribute("selectedType", "actAd");
        model.addAttribute("showReview", true);     
        return "back_end/logined/ad/adminListAllActAd";
    }
    
  //完成審核
    @PostMapping("/admin/actAd/reviewActAd")
    public String reviewActAd(@RequestParam Integer actAdId,
                              @RequestParam String remark,
                              @RequestParam String action) {

        int status = "pass".equals(action) ? 4 : 3; // 4=待繳費, 3=不通過

        actAdService.updateStatus(actAdId, status, remark);

        return "redirect:/admin/actAd/list";
    }
    
 // 進入修改頁面
    @GetMapping("admin/actAd/showUpdateActAd")
    public String ShowUpdateActAd(@RequestParam Integer actAdId, Model model){
    	model.addAttribute("actAdVO", actAdService.getOneActAd(actAdId));
    	model.addAttribute("actAdId", actAdId);  
        return "back_end/logined/ad/adminUpdateActAd";
    }
    
    // 修改頁面提交
    @PostMapping("admin/actAd/updateActAd")
    public String updateActAd(@RequestParam Integer actAdId,
    						  @RequestParam("adImg") MultipartFile file,
    						  @RequestParam Integer actAdRevStat, 
    						  @RequestParam String actAdRevRemark, 
    						  @RequestParam Integer actAdLaunStat,
    						  @RequestParam Integer actAdFee, 
    						  @RequestParam String actAdStart,
    						  @RequestParam String actAdEnd,
    						  @RequestParam String actAdFeeEnd
    						  ) throws IOException {
    
    	java.sql.Date Start = (actAdStart == null || actAdStart.isBlank())
    	        ? null
    	        : java.sql.Date.valueOf(actAdStart); 
    	
    	java.sql.Date End = (actAdEnd == null || actAdEnd.isBlank())
    	        ? null
    	        : java.sql.Date.valueOf(actAdEnd); 
    	
    	java.sql.Date feeEnd = (actAdFeeEnd == null || actAdFeeEnd.isBlank())
    	        ? null
    	        : java.sql.Date.valueOf(actAdFeeEnd); 
    	
    	
    	byte[] adImg = file.getBytes();
    	actAdService.updateActAd(actAdId, adImg ,actAdRevStat,actAdRevRemark,actAdLaunStat,Start,End,actAdFee,feeEnd);
    	return "redirect:/admin/actAd/list";
    }
    
//  ******************************小農功能**************************************
    //小農廣告功能首頁(在商品廣告Controller中)
    
    
    	
    
 // 小農申請商品廣告頁面顯示
    @GetMapping("fmem/actAd/applyAdView")
    public String showActAdList(ModelMap model, HttpSession session) {
        Integer fmemId = (Integer) session.getAttribute("fmemId");
        // 查出這位小農名下的活動清單
        List<Act> fmemAct = actAdService.findFmemAct(fmemId);
        
        model.addAttribute("selectedType", "actAd");
        model.addAttribute("actAdVO", new ActAdVO());
        model.addAttribute("act", fmemAct); 
        return "front_end/farmer/logined/fmemAd/farmerApplyActAd";
    }

 // 小農申請商品廣告送出
    @PostMapping("fmem/actAd/applyActAd")
    public String farmerApplyActAd(
            @ModelAttribute("actAdVO") ActAdVO actAdVO,
            BindingResult binding,
            @RequestParam("adImg") MultipartFile file,
            HttpSession session,
            ModelMap model
    ) throws IOException {
    	
        Integer fmemId = (Integer) session.getAttribute("fmemId");

        if (file.isEmpty()) {
            binding.rejectValue("actAdImg", "NotNull", "請選擇圖片!");
        } else {
            actAdVO.setActAdImg(file.getBytes());
        }    

        if (binding.hasErrors()) {
            model.addAttribute("act", actAdService.findFmemAct(fmemId));
            return "front_end/farmer/logined/fmemAd/farmerApplyActAd";
        }

        // 將取得的小農跟商品ID轉型
        Fmem fmemRef = actAdService.getFmemRef(fmemId);
        Act actRef = actAdService.getActRef(actAdVO.getActId());
        
        // 存入商品廣告DB
        actAdVO.setFmem(fmemRef);
        actAdVO.setAct(actRef);
        actAdService.addActAd(actAdVO);
        return "redirect:/fmem/actAd/list";
    }
    
    
  //小農查詢廣告列表
    @GetMapping("fmem/actAd/list")
    public String farmerListActAd(Model model,HttpSession session) {
    	Integer fmemId = (Integer) session.getAttribute("fmemId");
    	
    	model.addAttribute("selectedType", "actAd");
        model.addAttribute("listActAd", actAdService.findByFmemId(fmemId));
        return "front_end/farmer/logined/fmemAd/farmerListActAd";

	}
    
    
 // 小農付款：直接呼叫 LINE Pay Sandbox API
    @GetMapping("fmem/actAd/pay")
    public String showFarmerPayView(Model model,@RequestParam Integer actAdId) throws Exception {
        ActAdVO vo = actAdService.getOneActAd(actAdId);  // 取得廣告資料
        model.addAttribute("actAdVO",vo);
        // ===== 1. 組出 LINE Pay 的請求內容 =====
        String body = """
        		{
        		  "amount": %d,
        		  "currency": "TWD",
        		  "orderId": "ACTAD-%d",
        		  "packages": [{
        		    "id": "PKG1",
        		    "amount": %d,
        		    "name": "小農廣告上架費",
        		    "products": [{
        		      "name": "廣告上架費",
        		      "quantity": 1,
        		      "price": %d
        		    }]
        		  }],
        		  "redirectUrls": {
        		    "confirmUrl": "http://localhost:8080/fmem/actAd/return?actAdId=%d",
        		    "cancelUrl": "http://localhost:8080/fmem/actAd/cancel"
        		  }
        		}
        		""".formatted(vo.getActAdFee(), actAdId, vo.getActAdFee(), vo.getActAdFee(), actAdId);


        // ===== 2. 簽章 =====
        String base = "https://sandbox-api-pay.line.me";
        String path = "/v3/payments/request";
        String nonce = UUID.randomUUID().toString();
        String secret = "6e21d7668a02ac0e7f457fbf1bddd4e4"; //商家簽章密碼
        String sig = sign(secret + path + body + nonce);
        String channelId = "2008230869";  //商家ID

        // ===== 3. 呼叫 LINE Pay Request API =====
        WebClient client = WebClient.create();
        Map<String, Object> r = client.post().uri(base + path)
        	    .header("X-LINE-ChannelId", channelId)
        	    .header("X-LINE-Authorization-Nonce", nonce)
        	    .header("X-LINE-Authorization", sig)
        	    .contentType(MediaType.APPLICATION_JSON)
        	    .bodyValue(body)
        	    .retrieve().bodyToMono(Map.class).block();
        // ===== 4. 取得付款頁網址並導轉 =====
        String url = ((Map)((Map)r.get("info")).get("paymentUrl")).get("web").toString();
        return "redirect:" + url;
    }

    // ===== 5. 回呼後更新狀態 =====
    @GetMapping("fmem/actAd/return")
    public String linePayReturn(@RequestParam String transactionId,
                                @RequestParam String orderId,
                                @RequestParam Integer actAdId,
                                Model model) {
    	
    	ActAdVO actAdVO = actAdService.getOneActAd(actAdId);
    	model.addAttribute("actAdVO", actAdVO);
    	actAdService.updatePayAd(actAdVO);
        return "front_end/farmer/logined/fmemAd/farmerPaySuccess";
    }

    private String sign(String msg) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec("6e21d7668a02ac0e7f457fbf1bddd4e4".getBytes(), "HmacSHA256"));
        return Base64.getEncoder().encodeToString(mac.doFinal(msg.getBytes()));
    }
    
    
    // 原本要做假
//  @PostMapping("fmem/proAd/pay")
//  public String farmerPayAdFee(@RequestParam Integer proAdId) {
//  	
//  	 ProAdVO proAdVO = proAdService.getOneProAd(proAdId);
//  	 proAdService.updatePayAd(proAdVO);
//  	 
//		return "redirect:/fmem/proAd/list";
//  }
  
  
//******************************消費者顯示前端功能(預計另開一隻controller)**************************************
//  @GetMapping("/pro/index")
//  public String showAdCarousel(Model model) {
//      model.addAttribute("adIds", proAdService.getPassProAds());
//      return "front_end/act-index";
//  }
    
    
    
    //活動廣告圖片顯示
    @GetMapping(value = "/actAd/img/{id}")
    @ResponseBody
    public byte[] img(@PathVariable Integer id) {
        return actAdService.getImgBytes(id);
    }
}
