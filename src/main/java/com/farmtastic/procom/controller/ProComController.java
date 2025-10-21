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
	public String insertProCom(@RequestParam("proOrdId") Integer proOrdId, @RequestParam("proId") Integer proId,
			@RequestParam("proComRate") Byte proComRate, @RequestParam("proComContent") String proComContent,
			Model model, RedirectAttributes redirectAttrs) {

		// 查詢該訂單是否存在
		ProOrderVO proOrderVO = proOrdSvc.getOneProOrder(Integer.valueOf(proOrdId));

		// 開始新增評論資料
		ProComVO proComVO = new ProComVO();
		proComVO.setMemVO(proOrderVO.getMemVO());
		Pro proVO = proSvc.getOnePro(proId);
		proComVO.setProVO(proVO);
		proComVO.setProComRate(proComRate);
		proComVO.setProComContent(proComContent.trim());

		// 設置評論時間為當下日期
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
		proComVO.setProComTime(currentTimestamp);

		try {
			// 新增評論至資料庫
			proComSvc.addProCom(proComVO);

			redirectAttrs.addFlashAttribute("successMessage", "商品評論新增成功！");
		} catch (Exception e) {

			redirectAttrs.addFlashAttribute("errorMessage", "評論新增失敗：" + e.getMessage());
		}

		// 回商城商品頁面
		return "redirect:/mall/product/" + proId;

	}

}
