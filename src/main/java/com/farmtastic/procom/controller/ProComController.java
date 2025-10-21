package com.farmtastic.procom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmtastic.pro.model.Pro;
import com.farmtastic.pro.model.ProService;
import com.farmtastic.procom.model.ProComService;
import com.farmtastic.procom.model.ProComVO;
import com.farmtastic.proorder.model.ProOrderService;
import com.farmtastic.proorder.model.ProOrderVO;
import com.farmtastic.proorderitem.model.ProOrderItemService;
import com.farmtastic.proorderitem.model.ProOrderItemVO;

@Controller
@RequestMapping("/mem/proorders/procom")
public class ProComController {

	@Autowired
	ProComService proComSvc;
	@Autowired
	ProOrderService proOrdSvc;
	@Autowired
	ProOrderItemService proOrdItemSvc;
	@Autowired
	ProService proSvc;

	// 測試首頁
	@GetMapping("/")
	public String index() {

		return "/front_end/customer/unlogined/procom/proComTest";
	}

	@PostMapping("add")
	public String addProCom(@RequestParam("proOrdId") String proOrdId, ModelMap model) {
		// 查詢該訂單是否存在
		ProOrderVO proOrderVO = proOrdSvc.getOneProOrder(Integer.valueOf(proOrdId));

		if (proOrderVO == null) {
			model.addAttribute("errorMessage", "查無此訂單！無法評價！");
			return "/front_end/customer/logined/memProOrders/listAllProOrder";
		}

		// 查詢該訂單之明細
		List<ProOrderItemVO> proOrdItemList = proOrdItemSvc.getProOrderItems(proOrderVO);

		model.addAttribute("proOrdId", proOrdId);
		model.addAttribute("proOrdItemList", proOrdItemList);

		return "/front_end/customer/logined/proCom/addProCom";
	}

	@PostMapping("insert")
	public String insertProCom(@RequestParam("proOrdId") Integer proOrdId, // 接收訂單ID
			@RequestParam("proId") Integer proId, // 接收商品ID (每個表單特有)
			@RequestParam("proComRate") Byte proComRate, // 接收評分
			@RequestParam("proComContent") String proComContent, // 接收評論內容
			Model model, RedirectAttributes redirectAttrs // 用於重定向後傳遞訊息
	) {

		// 查詢該訂單是否存在
		ProOrderVO proOrderVO = proOrdSvc.getOneProOrder(Integer.valueOf(proOrdId));
		
		// 開始新增評論資料
		ProComVO proComVO = new ProComVO();
		// 取得會員資訊
		proComVO.setMemVO(proOrderVO.getMemVO()); 
		// 設置商品資訊
        Pro proVO = proSvc.getOnePro(proId);
		proComVO.setProVO(proVO); 
		// 設置評分和內容
		proComVO.setProComRate(proComRate);
		proComVO.setProComContent(proComContent.trim());

		// 設置評論時間為當下日期
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
		proComVO.setProComTime(currentTimestamp);

		try {
			// 4. 新增評論至資料庫
			proComSvc.addProCom(proComVO);
            // 設置成功訊息 (使用 flash attribute 可以在重定向後顯示)
			redirectAttrs.addFlashAttribute("successMessage", "商品評論新增成功！");
		} catch (Exception e) {
            // 處理新增失敗
			redirectAttrs.addFlashAttribute("errorMessage", "評論新增失敗：" + e.getMessage());
		}


        // 5. 重定向回訂單列表或您希望的頁面
        // 注意: 這裡不能使用 model.addAttribute()，因為是重定向，需要使用 RedirectAttributes
//		return "redirect:/mem/proorders/listAllProOrder";
		return "redirect:/mall/product/"+proId;
		
	}
	
}
