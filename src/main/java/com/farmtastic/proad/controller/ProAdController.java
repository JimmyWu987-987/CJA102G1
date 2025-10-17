package com.farmtastic.proad.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.util.Map;   
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.proad.model.ProAdService;
import com.farmtastic.proad.model.ProAdVO;
import com.farmtastic.pro.model.Pro;

import jakarta.servlet.http.HttpServletRequest;
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
    						  @RequestParam String proAdStart,
    						  @RequestParam String proAdEnd,
    						  @RequestParam String proAdFeeEnd
    						  ) throws IOException {
    
    	java.sql.Date Start = (proAdStart == null || proAdStart.isBlank())
    	        ? null
    	        : java.sql.Date.valueOf(proAdStart); 
    	
    	java.sql.Date End = (proAdEnd == null || proAdEnd.isBlank())
    	        ? null
    	        : java.sql.Date.valueOf(proAdEnd); 
    	
    	java.sql.Date feeEnd = (proAdFeeEnd == null || proAdFeeEnd.isBlank())
    	        ? null
    	        : java.sql.Date.valueOf(proAdFeeEnd); 
    	
    	
    	byte[] adImg = file.getBytes();
    	proAdService.updateProAd(proAdId, adImg ,proAdRevStat,proAdRevRemark,proAdLaunStat,Start,End,proAdFee,feeEnd);
    	return "redirect:/admin/proAd/list";
    }
    
    
    
//    ******************************小農功能**************************************
    //小農廣告功能首頁
    @GetMapping("fmem/proAd")
    public String showAdArea() {
    	return "front_end/farmer/logined/fmemAd/farmerAdArea";
    }
    // 小農申請商品廣告頁面顯示
    @GetMapping("fmem/proAd/applyAdView")
    public String showProAdList(ModelMap model, HttpSession session) {
        Integer fmemId = (Integer) session.getAttribute("fmemId");
        // 查出這位小農名下的商品清單
        List<Pro> fmemProducts = proAdService.findFmemProducts(fmemId);
        
        model.addAttribute("selectedType", "proAd");
        model.addAttribute("proAdVO", new ProAdVO());
        model.addAttribute("products", fmemProducts); 
        return "front_end/farmer/logined/fmemAd/farmerApplyProAd";
    }
    
    
 // 小農申請商品廣告送出
    @PostMapping("fmem/proAd/applyProAd")
    public String farmerApplyProAd(
            @ModelAttribute("proAdVO") ProAdVO proAdVO,
            BindingResult binding,
            @RequestParam("adImg") MultipartFile file,
            HttpSession session,
            ModelMap model
    ) throws IOException {
    	
        Integer fmemId = (Integer) session.getAttribute("fmemId");

        if (file.isEmpty()) {
            binding.rejectValue("proAdImg", "NotNull", "請選擇圖片!");
        } else {
            proAdVO.setProAdImg(file.getBytes());
        }    

        if (binding.hasErrors()) {
            model.addAttribute("products", proAdService.findFmemProducts(fmemId));
            return "front_end/farmer/logined/fmemAd/farmerApplyProAd";
        }

        // 將取得的小農跟商品ID轉型
        Fmem fmemRef = proAdService.getFmemRef(fmemId);
        Pro productRef = proAdService.getProductRef(proAdVO.getProId());
        
        // 存入商品廣告DB
        proAdVO.setFmem(fmemRef);
        proAdVO.setProduct(productRef);
        proAdService.addProAd(proAdVO);
        return "redirect:/fmem/proAd/list";
    }
    
    //小農查詢廣告列表
    @GetMapping("fmem/proAd/list")
    public String farmerListProAd(Model model,HttpSession session) {
    	Integer fmemId = (Integer) session.getAttribute("fmemId");
    	
    	model.addAttribute("selectedType", "proAd");
        model.addAttribute("listProAd", proAdService.findByFmemId(fmemId));
        return "front_end/farmer/logined/fmemAd/farmerListProAd";

	}
    
    
 // 小農付款：直接呼叫 LINE Pay Sandbox API
    @GetMapping("fmem/proAd/pay")
    public String showFarmerPayView(Model model,@RequestParam Integer proAdId,HttpServletRequest request) throws Exception {
        ProAdVO vo = proAdService.getOneProAd(proAdId);  // 取得廣告資料
        model.addAttribute("proAdVO",vo);
        // ===== 1. 組出 LINE Pay 的請求內容 =====
        String dynamicUrl = request.getScheme() +"://"+request.getServerName()+":"+request.getServerPort();
        String body = """
        		{
        		  "amount": %d,
        		  "currency": "TWD",
        		  "orderId": "PROAD-%d",
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
        		    "confirmUrl": "%s/fmem/proAd/return?proAdId=%d",
        		  	"cancelUrl": "%s/fmem/proAd/cancel"
        		  }
        		}
        		""".formatted(vo.getProAdFee(), proAdId, vo.getProAdFee(), vo.getProAdFee(), dynamicUrl, proAdId, dynamicUrl);


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
    @GetMapping("fmem/proAd/return")
    public String linePayReturn(@RequestParam String transactionId,
                                @RequestParam String orderId,
                                @RequestParam Integer proAdId,
                                Model model) {
    	
        // TODO: 可在此更新 proAd 狀態 → 已繳費
    	ProAdVO proAdVO = proAdService.getOneProAd(proAdId);
    	model.addAttribute("proAdVO", proAdVO);
    	proAdService.updatePayAd(proAdVO);
        return "front_end/farmer/logined/fmemAd/farmerPaySuccess";
    }

    private String sign(String msg) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec("6e21d7668a02ac0e7f457fbf1bddd4e4".getBytes(), "HmacSHA256"));
        return Base64.getEncoder().encodeToString(mac.doFinal(msg.getBytes()));
    }

    
    // 原本要做假
//    @PostMapping("fmem/proAd/pay")
//    public String farmerPayAdFee(@RequestParam Integer proAdId) {
//    	
//    	 ProAdVO proAdVO = proAdService.getOneProAd(proAdId);
//    	 proAdService.updatePayAd(proAdVO);
//    	 
//		return "redirect:/fmem/proAd/list";
//    }
    
    
//  ******************************消費者顯示前端功能(預計另開一隻controller)**************************************
//    @GetMapping("/pro/index")
//    public String showAdCarousel(Model model) {
//        model.addAttribute("adIds", proAdService.getPassProAds());
//        return "front_end/act-index";
//    }
    
    
    
    
    
    
//    *****************************相關功能**************************************
    //商品廣告圖片顯示
    @GetMapping(value = "/proAd/img/{id}")
    @ResponseBody
    public byte[] img(@PathVariable Integer id) {
        return proAdService.getImgBytes(id);
    }
 // 商城廣告點擊 → 轉導到商品頁
    @GetMapping("mall/proAd/{adId}")
    public String redirectAdToProduct(@PathVariable Integer adId) {
        ProAdVO ad = proAdService.getOneProAd(adId);
        if (ad == null || ad.getProduct() == null) {
            // 找不到就回商城首頁或你要的頁面
            return "redirect:/mall/products";
        }
        Integer proId = ad.getProduct().getProId(); // 依你的 VO 取商品ID
        return "redirect:/mall/product/" + proId;   // 不改商品 controller 的情況下直接轉導
    }

}
