package com.farmtastic.proorder.controller;

import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.proorder.model.ProOrderSevice;
import com.farmtastic.proorder.model.ProOrderVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mem/proorders")
public class LinePayController {

	@Autowired
	ProOrderSevice proOrdSvc;

	@GetMapping("linepayview")
	public String showLinePayView(Model model, HttpServletRequest request, @RequestParam Integer proOrdId)
			throws Exception {

		ProOrderVO proOrderVO = proOrdSvc.getOneProOrder(proOrdId);
		
		int amount = proOrderVO.getProOrdGrandTotal();
		int orderId = proOrdId;
		String dynamicUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		// ===== 1. 組出 LINE Pay 的請求內容 =====
		// confirmUrl: 導向成功的頁面
		// cancelUrl: 導向失敗的頁面
		String bodyTample = """
				{
				  "amount": %d,
				  "currency": "TWD",
				  "orderId": "PROORDER-%d",
				  "packages": [{
				    "id": "PKG1",
				    "amount": %d,
				    "name": "小農商品訂單實付金額",
				    "products": [{
				      "name": "訂單實付金額",
				      "quantity": 1,
				      "price": %d
				    }]
				  }],
				  "redirectUrls": {
				    "confirmUrl": "%s/mem/proorders/linepaysuccess?proOrdId=%d",
				   	"cancelUrl": "%s/mem/proorders/linepayerror?proOrdId=%d"
				  }
				}
				""";
		// 使用 .formatted() 方法，依序傳入對應的變數
		// Java 15+
		String body = bodyTample.formatted(
				amount,
				orderId, 
				amount,
				amount,
				dynamicUrl, // For confirmUrl base
				orderId,    // For confirmUrl proOrdId
				dynamicUrl, // For cancelUrl base
				orderId     // For cancelUrl proOrdId
				);
		
		 // ===== 2. 簽章 =====
        String base = "https://sandbox-api-pay.line.me";
        String path = "/v3/payments/request";
        String nonce = UUID.randomUUID().toString();
        String secret = "c1d9c6e0e0a0d3f4f821027d7d9bb370"; //商家簽章密碼
        String sig = sign(secret + path + body + nonce);
        String channelId = "2008275260";  //商家ID

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

    private String sign(String msg) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec("c1d9c6e0e0a0d3f4f821027d7d9bb370".getBytes(), "HmacSHA256")); //商家簽章密碼 
        return Base64.getEncoder().encodeToString(mac.doFinal(msg.getBytes()));
    }
    
    // linepay 成功
    @GetMapping("linepaysuccess")
    String linepPaySuccess(@RequestParam Integer proOrdId, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
    	 	
       	// 回傳新增訂單詢息給訊息回去 ProOrderMemController.java
    	session.setAttribute("proOrdIdByPay", proOrdId);
    	
    	return "redirect:/mem/proorders/dopay";
    }
    
    // linepay 失敗
    @GetMapping("linepayerror")
    String linePaySuccess(@RequestParam Integer proOrdId, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
    	 	
       	// 刪除訂單資料，傳導至購物車，重新下單。
    	session.removeAttribute("cartToProOrder");
    	proOrdSvc.deleteProOrder(proOrdId);
    	redirectAttributes.addFlashAttribute("errorMessage", "Linepay付款失敗，請重新下單！");
    	
    	return "redirect:/cart/view";
    }
    

}
